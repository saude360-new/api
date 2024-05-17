import type { Dict } from 'typesdk/types';
import List, { type ReadonlyStorageBlock } from 'typesdk/list';
import { Crypto, Hash, LayeredEncryption } from 'typesdk/crypto';
import { jsonSafeParser, jsonSafeStringify } from 'typesdk/safe-json';
import { parseTimeString, type DateTimeString } from 'typesdk/datetime';

import env from '@shared/infra/env';
import { connect } from '@shared/lib/database';
import Entity from '@shared/core/domain/entity';
import ExpiredError from '@shared/errors/ExpiredError';
import Slicer, { type Chunk } from '@shared/lib/Slicer';
import { IPv4, IPv6, type IP } from '@shared/core/inet';
import { aesEncrypt, aesDecrypt } from '@shared/lib/crypto';
import InvalidSignatureError from '@shared/errors/InvalidSignatureError';



export type SessionDocument<T> = {
  readonly payload: T;
  readonly headers: {
    readonly [key: string]: string | number;
    readonly length: number;
  };
  readonly signature: string;
  readonly createdAt: string;
  readonly expiresAt: string;
  readonly ip?: {
    readonly family: 'IPv4' | 'IPv6';
    readonly address: string;
  };
  readonly userId?: string;
  readonly sessionId: string;
};


type SessionProps<T> = {
  payload: T;
  userId?: string;
  createdAt?: string;
  signature?: string;
  headers?: Dict<any>;
  ipAddress?: string | IPv4 | IPv6;
  expires: DateTimeString | Omit<string, DateTimeString>;
}


export class Session<T extends Dict<any>> extends Entity<SessionProps<T>> {
  public get payload(): T {
    return this.props.payload;
  }

  public get headers(): Dict<string | number> & { readonly length: number } {
    if(!this.props.headers || 
      !Object.prototype.hasOwnProperty.call(this.props.headers, 'length')) return { length: 0 };

    return this.props.headers as unknown as Dict<string | number> & { readonly length: number };
  }

  public get signature(): string {
    return this.props.signature!;
  }

  public get createdAt(): Date {
    return new Date(this.props.createdAt!);
  }

  public get expiresAt(): Date {
    if(/^(\d+)([smhdwMy])$/.test(this.props.expires as string)) return parseTimeString(this.props.expires as DateTimeString);
    return new Date(this.props.expires as string);
  }

  public get ip(): IP | null {
    if(!this.props.ipAddress) return null;
    
    if(this.props.ipAddress instanceof IPv4) return {
      family: 'IPv4',
      ip: this.props.ipAddress,
    };

    if(this.props.ipAddress instanceof IPv6) return {
      family: 'IPv6',
      ip: this.props.ipAddress,
    };

    const parseIpv6 = (ip: string): IP => {
      // const [address, scopeId] = ip.split('%');
      const i = IPv6.from(ip);

      if(i.isLeft()) {
        throw i.value;
      }

      return {
        family: 'IPv6',
        ip: i.value,
      };
    };

    const parseIpv4 = (ip: string): IP => {
      const i = IPv4.from(ip);
    
      if(i.isLeft()) {
        throw i.value;
      }
    
      return {
        family: 'IPv4',
        ip: i.value,
      };
    };

    if(this.props.ipAddress.indexOf(':') > -1) return parseIpv6(this.props.ipAddress);
    return parseIpv4(this.props.ipAddress);
  }

  public get userId(): string | null {
    return this.props.userId ?? null;
  }

  public get sessionId(): string {
    return this._id;
  }

  private constructor(props: SessionProps<T>, id?: string) {
    super(props, id);
  }

  public static async create<T extends Dict<any>>(props: SessionProps<T>): Promise<Session<T>> {
    let query = `INSERT INTO sessions (session_id,
      headers,
      created_at,
      expires_at,
      payload_hash,
      payload_merkle_root,
      signature`;

    if(props.ipAddress) {
      query += ', ip_address';
    }

    if(props.userId) {
      query += ', user_id';
    }

    query += ') VALUES ($1, $2, $3, $4, $5, $6, $7';

    if(props.ipAddress && props.userId) {
      query += ', $8, $9)';
    } else if(props.ipAddress || props.userId) {
      query += ', $8)';
    } else {
      query += ')';
    }

    query += ' RETURNING *';
    
    const signature = await Crypto.hmac512(jsonSafeStringify(props.payload) ?? '{}', env.getEnvironmentVariable('HMAC_KEY')!);
    const enc = new LayeredEncryption({
      layers: 3,
      algorithm: 'aes-256-cbc',
      key: env.getEnvironmentVariable('SYMMETRIC_ENC_KEY')!,
    });

    const payload = await enc.encrypt(props.payload);
    const payloadSlicer = new Slicer(payload, 512);

    payloadSlicer.slice();
    const merkleTree = payloadSlicer.createMerkleTree();

    const h = jsonSafeStringify({
      ...props.headers,
      merkleRoot: merkleTree[0],
      length: Buffer.from(jsonSafeStringify(props.payload) ?? '{}').byteLength,
    });

    const values = [
      Crypto.uuid().replace(/-/g, ''),
      h,
      new Date().toISOString(),
      parseTimeString(props.expires as DateTimeString).toISOString(),
      payloadSlicer.hash,
      merkleTree[0],
      signature,
    ] as any[];

    if(props.ipAddress) {
      const ip = await aesEncrypt(typeof props.ipAddress === 'string' ?
        props.ipAddress : 
        props.ipAddress.address, 'base64');

      values.push(ip);
    }

    if(props.userId) {
      values.push(props.userId);
    }

    const database = await connect();
    
    try {
      await database.transaction(async client => {
        await client.query({
          text: query,
          values,
        });
  
        for(const paylaodChunk of payloadSlicer.chunks) {
          await client.query({
            text: `INSERT INTO session_payload_chunks (session_id,
              chunk_index,
              chunk_value,
              chunk_hash) VALUES ($1, $2, $3, $4)`,
            values: [
              values[0],
              paylaodChunk.index,
              paylaodChunk.value,
              paylaodChunk.hash,
            ],
          });
        }
      });

      const parsedHeaders = jsonSafeParser<any>(h!);

      if(parsedHeaders.isLeft()) {
        throw parsedHeaders.value;
      }
      
      const s: SessionProps<T> = {
        payload: props.payload,
        headers: parsedHeaders.value,
        createdAt: values[2]!,
        expires: values[3]!,
        signature,
      };
  
      if(props.ipAddress) {
        s.ipAddress = props.ipAddress;
      }
  
      if(props.userId) {
        s.userId = props.userId;
      }
  
      return new Session<T>(s, values[0]!);
    } finally {
      await database.close();
    }
  }

  public static async find<T extends Dict<any>>(sessionId: string): Promise<Session<T> | null> {
    const database = await connect();
    
    try {
      const result = await database.query('SELECT * FROM sessions WHERE session_id = $1', {
        values: [sessionId],
      });
  
      if(result.rows.length !== 1) return null;
  
      if(new Date(result.rows[0].expires_at) < new Date()) {
        await database.query('DELETE FROM sessions WHERE session_id = $1', {
          values: [sessionId],
        });
      
        await database.close();
        throw new ExpiredError('Session has expired.', new Date(result.rows[0].expires_at));
      }
  
      const payloadChunksResult = await database.query('SELECT * FROM session_payload_chunks WHERE session_id = $1', {
        values: [sessionId],
      });
  
      const payloadChunks: Chunk[] = payloadChunksResult.rows.map(row => ({
        index: Number(row.chunk_index),
        value: row.chunk_value,
        hash: row.chunk_hash,
      }));
  
      const encryptedPayload = Slicer.join(payloadChunks);
      const payloadHash = Hash.sha512(encryptedPayload);
      
      if(payloadHash !== result.rows[0].payload_hash) {
        await database.query('DELETE FROM sessions WHERE session_id = $1', {
          values: [sessionId],
        });

        throw new InvalidSignatureError('Failed to verify the integrity of the payload.', result.rows[0].payload_hash, payloadHash);
      }
      
      const enc = new LayeredEncryption({
        layers: 3,
        algorithm: 'aes-256-cbc',
        key: env.getEnvironmentVariable('SYMMETRIC_ENC_KEY')!,
      });
  
      const { payload } = await enc.decrypt<T>(encryptedPayload);
      const payloadHmac = await Crypto.hmac512(jsonSafeStringify(payload) ?? '{}', env.getEnvironmentVariable('HMAC_KEY')!);
      
      if(payloadHmac !== result.rows[0].signature) {
        // await database.query('DELETE FROM sessions WHERE session_id = $1', {
        //   values: [sessionId],
        // });
      
        await database.close();
        throw new InvalidSignatureError('Failed to verify the integrity of the payload.', result.rows[0].signature, payloadHmac);
      }
  
      const s: SessionProps<T> = {
        payload,
        headers: result.rows[0].headers,
        createdAt: result.rows[0].created_at,
        expires: result.rows[0].expires_at,
        signature: result.rows[0].signature,
      };
  
      if(result.rows[0].ip_address) {
        const ip = await aesDecrypt<string>(Buffer.from(result.rows[0].ip_address, 'base64'));
        s.ipAddress = ip.payload;
      }
  
      if(result.rows[0].user_id) {
        s.userId = result.rows[0].user_id;
      }
  
      return new Session<T>(s, result.rows[0].session_id);
    } finally {
      await database.close();
    }
  }

  public static async findByIdAndDelete(sessionId: string): Promise<void> {
    const database = await connect();
    
    try {
      await database.query('DELETE FROM sessions WHERE session_id = $1', {
        values: [sessionId],
      });
    } finally {
      await database.close();
    }
  }

  public static async findByUserId<T extends Dict<any>>(userId: string): Promise<List<Session<T>>> {
    const database = await connect();
    
    try {
      const results = await database.query('SELECT * FROM sessions WHERE user_id = $1', {
        values: [userId],
      });
  
      const list = new List<Session<T>>();
      if(results.rows.length < 1) return list;
  
      for(const row of results.rows) {
        const session = await Session.find<T>(row.session_id);
        if(!session) continue;
  
        list.push(session);
      }
  
      return list;
    } finally {
      await database.close();
    }
  }

  public static async count(): Promise<number> {
    const database = await connect();
    
    try {
      const result = await database.query('SELECT COUNT(*) FROM sessions');
      return parseInt(String(result.rows[0].count));
    } finally {
      await database.close();
    }
  }

  public static async countFromUser(userId: string): Promise<number> {
    const database = await connect();
    
    try {
      const result = await database.query('SELECT COUNT(*) FROM sessions WHERE user_id = $1', {
        values: [userId],
      });
  
      await database.close();
      return parseInt(String(result.rows[0].count));
    } finally {
      await database.close();
    }
  }

  public static async all<T extends Dict<any>>(): Promise<List<Session<T>>> {
    const database = await connect();
    
    try {
      const results = await database.query('SELECT * FROM sessions');

      const list = new List<Session<T>>();

      for(const row of results.rows) {
        const session = await Session.find<T>(row.session_id);
        if(!session) continue;

        list.push(session);
      }
    
      return list;
    } finally {
      await database.close();
    }
  }

  public static convertListToDocument<T extends Dict<any>>(sessions: List<Session<T>>): List<SessionDocument<T>> {
    const list = new List<SessionDocument<T>>();
    let node: ReadonlyStorageBlock<Session<T> | null> | null = sessions.tree();

    while(node) {
      if(node.value) {
        list.push(node.value.doc());
      }

      node = node.next;
    }

    return list;
  }

  public static convertListToDocumentArray<T extends Dict<any>>(sessions: List<Session<T>>): SessionDocument<T>[] {
    return this.convertListToDocument(sessions).toArray();
  }

  public doc(): SessionDocument<T> {
    return Object.freeze({
      payload: this.payload,
      headers: this.headers,
      signature: this.signature,
      createdAt: this.createdAt.toISOString(),
      expiresAt: this.expiresAt.toISOString(),
      userId: this.userId ?? undefined,
      sessionId: this._id,
      ip: this.ip ? {
        family: this.ip.family,
        address: this.ip.ip.address,
      } : undefined,
    } satisfies SessionDocument<T>);
  }

  public async erase(): Promise<void> {
    const database = await connect();
    
    try {
      await database.query('DELETE FROM sessions WHERE session_id = $1', {
        values: [this._id],
      });
  
      this._destroy();
    } finally {
      await database.close();
    }
  }

  private _destroy(): void {
    this.props.payload = null!;
    this.props.headers = null!;
    this.props.signature = null!;
    this.props.createdAt = null!;
    this.props.expires = null!;
    this.props.ipAddress = null!;
    this.props.userId = null!;
  }
}

export default Session;
