import { Crypto } from 'typesdk/crypto';
import NotFoundError from 'typesdk/errors/http/extended/NotFoundError';
import { parseTimeString, type DateTimeString } from 'typesdk/datetime';
import UnprocessableEntityError from 'typesdk/errors/http/extended/UnprocessableEntityError';

import User from '@models/users';
import { connect } from '@shared/lib/database';
import Entity from '@shared/core/domain/entity';


export type RefreshTokenDocument = {
  readonly userId: string;
  readonly tokenId: string;
  readonly expiresAt: string;
  readonly createdAt: string;
}

type RefreshTokenProps = {
  userId: string;
  expiresIn?: string;
  createdAt?: string;
}

export class RefreshToken extends Entity<RefreshTokenProps> {
  public get userId(): string {
    return this.props.userId;
  }

  public get tokenId(): string {
    return this._id;
  }

  public get expiresAt(): Date {
    if(!this.props.expiresIn) {
      throw new UnprocessableEntityError('RefreshToken expiresAt is undefined');
    }

    return new Date(this.props.expiresIn);
  }

  public get createdAt(): Date {
    return new Date(this.props.createdAt!);
  }

  private constructor(props: RefreshTokenProps, id?: string) {
    super(props, id);
  }

  public static async create(props: RefreshTokenProps): Promise<RefreshToken> {
    props.expiresIn ??= '2d';

    const user = await User.findById(props.userId);

    if(!user) {
      throw new UnprocessableEntityError('No user found with this id');
    }

    const query = `INSERT INTO refresh_tokens (user_id,
      token_id,
      created_at,
      expires_at) VALUES ($1, $2, $3, $4) RETURNING *`;

    const database = await connect();
    
    try {
      const result = await database.query(query, {
        values: [
          user.userId,
          Crypto.uuid().replace(/-/g, ''),
          new Date().toISOString(),
          parseTimeString(props.expiresIn as DateTimeString).toISOString(),
        ],
      });
      
      return new RefreshToken({
        userId: result.rows[0].user_id,
        createdAt: result.rows[0].created_at,
        expiresIn: result.rows[0].expires_at,
      }, result.rows[0].token_id);
    } finally {
      await database.close();
    }
  }

  public static async findById(tokenId: string): Promise<RefreshToken> {
    const query = 'SELECT * FROM refresh_tokens WHERE token_id = $1';

    const database = await connect();
    
    try {
      const result = await database.query(query, {
        values: [tokenId],
      });
  
      if(result.rows.length !== 1) {
        throw new NotFoundError('No refresh token found with this id');
      }
  
      return new RefreshToken({
        userId: result.rows[0].user_id,
        createdAt: result.rows[0].created_at,
        expiresIn: result.rows[0].expires_at,
      }, result.rows[0].token_id);
    } finally {
      await database.close();
    }
  }

  public static async findByUserId(userId: string): Promise<RefreshToken[]> {
    const query = 'SELECT * FROM refresh_tokens WHERE user_id = $1';

    const database = await connect();
   
    try {
      const result = await database.query(query, {
        values: [userId],
      });
  
      if(result.rows.length !== 1) {
        throw new NotFoundError('No refresh token found with this user id');
      }
  
      return result.rows.map((row) => new RefreshToken({
        userId: row.user_id,
        createdAt: row.created_at,
        expiresIn: row.expires_at,
      }, row.token_id));
    } finally {
      await database.close();
    }
  }

  public static async findByIdAndDelete(tokenId: string): Promise<void> {
    const query = 'DELETE FROM refresh_tokens WHERE token_id = $1';
    const database = await connect();
    
    try {
      await database.query(query, {
        values: [tokenId],
      });
    } finally {
      await database.close();
    }
  }

  public static async findByUserIdAndDelete(userId: string): Promise<void> {
    const query = 'DELETE FROM refresh_tokens WHERE user_id = $1';
    const database = await connect();
    
    try {
      await database.query(query, {
        values: [userId],
      });
    } finally {
      await database.close();
    }
  }

  public static async count(): Promise<number> {
    const database = await connect();
    
    try {
      const result = await database.query('SELECT COUNT(*) FROM refresh_tokens');
      return parseInt(result.rows[0].count);
    } finally {
      await database.close();
    }
  }

  public static async countFromUser(userId: string): Promise<number> {
    const database = await connect();
    
    try {
      const result = await database.query('SELECT COUNT(*) FROM refresh_tokens WHERE user_id = $1', {
        values: [userId],
      });
  
      return parseInt(result.rows[0].count);
    } finally {
      await database.close();
    }
  }
}

export default RefreshToken;
