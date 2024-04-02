import type { Dict } from 'typesdk/types';
import { Database } from 'typesdk/database/postgres';

import RedisClient from 'ioredis';
import env from '@shared/infra/env';



export type AbstractDatabaseSearchProps = {
  $where: Dict<string | number>;
  $or?: Dict<string | number>;
  $limit?: number;
  $offset?: number;
  $orderBy?: {
    column: string;
    order: 'ASC' | 'DESC';
  }
}

export function buildWhereClause(search: AbstractDatabaseSearchProps, columnAliases?: Dict<string>): string {
  const aliases = columnAliases ?? {};
  let fieldIndex = 1;
  
  const whereConditions = [];
  const orConditions = [];

  for(const prop in search.$where) {
    if(!Object.prototype.hasOwnProperty.call(search.$where, prop)) continue;
    let column = prop;

    if(Object.prototype.hasOwnProperty.call(aliases, prop)) {
      column = aliases[prop];
    }

    whereConditions.push(`${column} = $${fieldIndex++}`);
  }

  fieldIndex = 1;

  if(search.$or) {
    for(const prop in search.$or) {
      if(!Object.prototype.hasOwnProperty.call(search.$or, prop)) continue;
      let column = prop;
    
      if(Object.prototype.hasOwnProperty.call(aliases, prop)) {
        column = aliases[prop];
      }
    
      orConditions.push(`${column} = $${fieldIndex++}`);
    }
  }

  let output = whereConditions.join(' AND ');

  if(orConditions.length > 0) {
    output += ` OR ${orConditions.join(' OR ')}`;
  }

  if(search.$limit) {
    output += ` LIMIT ${search.$limit}`;

    if(search.$offset) {
      output += ` OFFSET ${search.$offset}`;
    }
  }

  if(search.$orderBy) {
    output += ` ORDER BY ${aliases[search.$orderBy.column] ?? search.$orderBy.column} ${search.$orderBy.order}`;
  }

  return output;
}


declare global {
  // eslint-disable-next-line no-var
  var db: Database | undefined;

  // eslint-disable-next-line no-var
  var redis: RedisClient | undefined;
}

export async function connect(): Promise<Database> {
  const uri = env.getEnvironmentVariable('POSTGRES_URL');

  if(!uri) {
    throw new Error('Missing required environment variable POSTGRES_URL');
  }

  if(env.isProduction()) return new Database(uri);

  if(!globalThis.db ||
    !(await globalThis.db.isOnline())) {
    globalThis.db = new Database(uri);
  }

  return globalThis.db;
}


export function safeCache(): RedisClient | undefined {
  const url = env.getEnvironmentVariable('REDIS_CACHE_URL');
  if(!url) return undefined;

  try {
    new URL(url);
  } catch {
    return undefined;
  }

  if(env.isProduction()) return new RedisClient(url);

  if(!globalThis.redis) {
    globalThis.redis = new RedisClient(url);
  }

  return globalThis.redis;
}


export function cache(): RedisClient {
  const client = safeCache();

  if(!client) {
    throw new Error('Missing required environment variables CACHE_REDIS_HOST and CACHE_REDIS_PORT');
  }

  return client;
}


export default connect;
