import * as openpgp from 'openpgp';
import { hmac } from 'cryptx-sdk/hash';
import { Exception } from 'typesdk/errors';
import { randomBytes } from 'cryptx-sdk/core';
import type { ReadonlyDict, Dict } from 'typesdk/types';
import { AES, SymmetricKey } from 'cryptx-sdk/symmetric';

import env from '@shared/infra/env';
import connect from '@shared/lib/database';
import Email from '@shared/core/domain/email';
import { aesEncrypt } from '@shared/lib/crypto';
import Entity from '@shared/core/domain/entity';
import Password from '@shared/core/domain/password';


export type PresetMetadata = {
  currency_code: string;
  currency_symbol: string;
  locale: string;
  timezone: string;
};

export type UserDocument = {
  readonly userId: string;
  readonly firstName: string;
  readonly lastName: string;
  readonly role: 'caregiver' | 'patient';
  readonly birthDate: string;
  readonly gender?: 'm' | 'f';
  readonly email: string;
  readonly password: string;
  readonly salt: string;
  readonly symmetricKey: string;
  readonly publicKey: string;
  readonly privateKey: string;
  readonly createdAt: string;
  readonly updatedAt: string;
  readonly metadata: ReadonlyDict<string | number | boolean | null> & Readonly<Partial<PresetMetadata>>;
};

export type SafeUserDocument = Omit<UserDocument, 'password' | 'salt' | 'symmetricKey' | 'privateKey'>;


type UserProps = {
  firstName: string;
  lastName: string;
  role?: 'caregiver' | 'patient';
  birthDate: string;
  gender?: 'm' | 'f';
  email: string;
  password: string;
  salt?: string;
  symmetricKey?: string;
  publicKey?: string;
  privateKey?: string;
  createdAt?: string;
  updatedAt?: string;
  metadata?: Dict<string | number | boolean | null> & Partial<PresetMetadata>;
};


export class User extends Entity<UserProps> {
  public get userId(): string {
    return this._id;
  }

  public get firstName(): string {
    return this.props.firstName;
  }

  public get lastName(): string {
    return this.props.lastName;
  }

  public get role(): 'caregiver' | 'patient' {
    return this.props.role!;
  }

  public get birthDate(): Date {
    return new Date(this.props.birthDate);
  }

  public get gender(): 'm' | 'f' | null {
    return this.props.gender ?? null;
  }

  public get email(): Email {
    const e = Email.create(this.props.email);

    if(e.isLeft()) {
      throw e.value;
    }

    return e.value;
  }

  public get password(): Password {
    const p = Password.create(this.props.password, true, this.salt);

    if(p.isLeft()) {
      throw p.value;
    }

    return p.value;
  }

  public get salt(): string {
    return this.props.salt!;
  }

  public get saltBuffer(): Buffer {
    return Buffer.from(this.props.salt!, 'hex');
  }

  public get symmetricKey(): Buffer {
    return Buffer.from(this.props.symmetricKey!, 'base64');
  }

  public get publicKey(): Buffer {
    return Buffer.from(this.props.publicKey!, 'base64');
  }

  public get privateKey(): Buffer {
    return Buffer.from(this.props.privateKey!, 'base64');
  }

  public get metadata(): Dict<string | number | boolean | null> & Partial<PresetMetadata> {
    this.props.metadata ??= {} as any;
    return { ...this.props.metadata };
  }

  public get createdAt(): Date {
    return new Date(this.props.createdAt!);
  }

  public get updatedAt(): Date {
    return new Date(this.props.updatedAt!);
  }

  private constructor(props: UserProps, id?: string) {
    super(props, id);
  }

  public setMetadata(key: string, value: string | number | boolean | null, override: boolean = true): void {
    this.props.metadata ??= {} as any;

    if(override || !this.props.metadata![key]) {
      this.props.metadata![key] = value;
    }
  }

  public getMetadata(key: string): string | number | boolean | null | undefined {
    return this.props.metadata![key];
  }

  public deleteMetadata(key: string): void {
    delete this.props.metadata![key];
  }

  public doc(): UserDocument {
    return Object.freeze<UserDocument>({
      userId: this.userId,
      firstName: this.firstName,
      lastName: this.lastName,
      role: this.role,
      birthDate: this.birthDate.toISOString(),
      createdAt: this.createdAt.toISOString(),
      email: this.email.value,
      metadata: Object.freeze(this.metadata),
      password: this.props.password,
      privateKey: this.privateKey.toString('base64'),
      publicKey: this.publicKey.toString('base64'),
      salt: this.salt,
      symmetricKey: this.symmetricKey.toString('base64'),
      updatedAt: this.updatedAt.toISOString(),
      gender: this.gender ?? undefined,
    });
  }

  public toSafeDocument(): SafeUserDocument {
    return Object.freeze<SafeUserDocument>({
      userId: this.userId,
      firstName: this.firstName,
      lastName: this.lastName,
      role: this.role,
      birthDate: this.birthDate.toISOString(),
      createdAt: this.createdAt.toISOString(),
      email: this.email.value,
      metadata: Object.freeze(this.metadata),
      updatedAt: this.updatedAt.toISOString(),
      publicKey: this.publicKey.toString('base64'),
      gender: this.gender ?? undefined,
    });
  }

  public static async create(props: Omit<UserProps, 'birthDate'> & { birthDate: Date | string; }): Promise<User> {
    if(!env.getEnvironmentVariable('ASYMMETRIC_PGP_SECRET')) {
      throw new Exception('ASYMMETRIC_PGP_SECRET is not defined');
    }

    const e = Email.create(props.email);

    if(e.isLeft()) {
      throw e.value;
    }

    const salt = Buffer.from((await randomBytes(128)));
    const passwd = Password.create(props.password, false, salt.toString('hex'));

    if(passwd.isLeft()) {
      throw passwd.value;
    }

    const ee = await aesEncrypt(e.value.value, 'base64');
    const eh = await hmac(Buffer.from(e.value.value),
      Buffer.from(env.getEnvironmentVariable('HMAC_KEY')!),
      'sha512');

    const { privateKey, publicKey } = await openpgp.generateKey({
      userIDs: [{
        name: `${props.firstName} ${props.lastName}`,
        email: props.email,
      }],
      passphrase: env.getEnvironmentVariable('ASYMMETRIC_PGP_SECRET')!,
      curve: 'ed25519',
      format: 'armored',
    });

    const sk = await randomBytes(128);

    const ski = new SymmetricKey(sk, {
      algorithm: {
        name: 'aes-256-cbc',
        length: 128,
      },
      usages: ['encrypt', 'decrypt'],
    });

    const aes = new AES(ski, 'aes-256-cbc');
    const esk = await aesEncrypt(Buffer.from(sk).toString('base64'), 'base64');

    const epk = (await aes.encrypt(publicKey)).toString('base64');
    const eppk = (await aes.encrypt(privateKey)).toString('base64');

    const database = await connect();

    const q = `INSERT INTO users (user_id,
      first_name,
      last_name,
      role,
      birth_date,
      gender,
      email_address,
      email_hash,
      password,
      salt,
      symmetric_key,
      public_key,
      private_key,
      created_at,
      updated_at) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15) RETURNING *`;

    try {
      const [results] = await database.transaction(async client => {
        const { rows } = await client.query({
          text: q,
          values: [
            Entity.generateId('uuid-without-dashes'),
            props.firstName,
            props.lastName,
            props.role ?? 'caregiver',
            props.birthDate instanceof Date ? props.birthDate.toISOString() : props.birthDate,
            props.gender as any ?? null,
            ee,
            Buffer.from(eh).toString('hex'),
            (await passwd.value.getHashedValue()),
            salt.toString('hex'),
            esk,
            epk,
            eppk,
            new Date().toISOString(),
            new Date().toISOString(),
          ],
        });

        await client.query({
          text: `INSERT INTO user_metadata (user_id,
            metadata_key,
            metadata_value) VALUES ($1, $2, $3), ($1, $4, $5), ($1, $6, $7), ($1, $8, $9)`,
          values: [
            rows[0].user_id,
            'currency_code',
            'BRL',
            'currency_symbol',
            'R$',
            'locale',
            'pt-BR',
            'timezone',
            'America/Sao_Paulo',
          ],
        });

        return [rows[0]];
      });

      return new User({
        birthDate: results.birth_date instanceof Date ? results.birth_date.toISOString() : results.birth_date,
        email: e.value.value,
        firstName: results.first_name,
        lastName: results.last_name,
        password: results.password,
        createdAt: results.created_at instanceof Date ? results.created_at.toISOString() : results.created_at,
        gender: results.gender,
        salt: results.salt,
        privateKey: results.private_key,
        publicKey: results.public_key,
        role: results.role,
        symmetricKey: results.symmetric_key,
        updatedAt: results.updated_at instanceof Date ? results.updated_at.toISOString() : results.updated_at,
        metadata: {
          currency_code: 'BRL',
          currency_symbol: 'R$',
          locale: 'pt-BR',
          timezone: 'America/Sao_Paulo',
        },
      });
    } finally {
      await database.close();
    }
  }
}

export default User;
