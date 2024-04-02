import type { Dict } from 'typesdk/types';
import { AES } from 'typesdk/crypto/symmetric';
import { CryptoKey, Hash } from 'typesdk/crypto';
import { assertDict } from 'typesdk/utils/assertions';

import env from '@shared/infra/env';


export async function encryptRequestBody(body: Dict<any>): Promise<Dict<any>> {
  const key = new CryptoKey(env.getEnvironmentVariable('T_BUFFER_KEY')!, {
    algorithm: {
      name: 'aes-256-cbc',
    },
    usages: ['encrypt', 'decrypt'],
  });
  
  const aes = new AES(key);

  const headers = {
    algorithm: Hash.sha256(key.algorithm.name as string),
    timestamp: Date.now(),
    length: Buffer.from(JSON.stringify(body)).byteLength,
    signature: Hash.sha512(JSON.stringify(body)),
  };

  const payload = await aes.encrypt(body);

  return Object.freeze({
    ...headers,
    payload,
  });
}



interface DecryptedBody<T> {
  readonly payload: T;
  readonly algorithm: string;
  readonly timestamp: number;
  readonly length: number;
  readonly signature: string;
  readonly cryptoSign: string;
  readonly age: number;
  readonly currentTimestamp: number;
  readonly encryptionTimestamp: number;
}

export async function decryptRequestBody<T>(body: Dict<any>): Promise<DecryptedBody<T>> {
  _assertEncryptedBody(body);

  const key = new CryptoKey(env.getEnvironmentVariable('T_BUFFER_KEY')!, {
    algorithm: {
      name: 'aes-256-cbc',
    },
    usages: ['encrypt', 'decrypt'],
  });
    
  const aes = new AES(key);
    
  const decrypted = await aes.decrypt(body.payload);
  
  return Object.freeze({
    age: decrypted.age,
    algorithm: body.algorithm,
    cryptoSign: decrypted.signature,
    currentTimestamp: decrypted.currentTimestamp,
    encryptionTimestamp: body.timestamp,
    length: body.length,
    payload: decrypted.payload,
    signature: body.signature,
    timestamp: Date.now(),
  }) as unknown as DecryptedBody<T>;
}


function _assertEncryptedBody(data: unknown): asserts data is Dict<any> {
  assertDict(data);

  if(Object.keys(data).length !== 5) {
    throw new Error(`Invalid encrypted body. Expected 6 keys received ${Object.keys(data).length}`);
  }

  if(!data.algorithm) {
    throw new Error('Missing algorithm');
  }

  if(!data.timestamp) {
    throw new Error('Missing timestamp');
  }

  if(!data.length) {
    throw new Error('Missing length');
  }

  if(!data.signature) {
    throw new Error('Missing signature');
  }

  if(!data.payload) {
    throw new Error('Missing payload');
  }

  if(typeof data.algorithm !== 'string') {
    throw new Error('Invalid algorithm');
  }

  if(typeof data.timestamp !== 'number') {
    throw new Error('Invalid timestamp');
  }

  if(typeof data.length !== 'number') {
    throw new Error('Invalid length');
  }

  if(typeof data.signature !== 'string') {
    throw new Error('Invalid signature');
  }

  if(typeof data.payload !== 'string') {
    throw new Error('Invalid payload');
  }
}
