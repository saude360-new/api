import { AES, SymmetricKey } from 'cryptx-sdk/symmetric';
import { type Decrypted } from 'cryptx-sdk/symmetric/core';

import env from '@shared/infra/env';


export function aesEncrypt(data: any): Promise<Uint8Array>;
export function aesEncrypt(data: any, encoding: BufferEncoding): Promise<string>;
export async function aesEncrypt(data: any, encoding?: BufferEncoding): Promise<string | Uint8Array> {
  if(!process.env.SYMMETRIC_ENC_KEY) {
    throw new Error('[SYMMETRIC_ENC_KEY] Unset encryption key');
  }
  
  const k = new SymmetricKey(Buffer.from(env.getEnvironmentVariable('SYMMETRIC_ENC_KEY')!), {
    algorithm: 'aes-256-cbc',
    usages: ['encrypt', 'decrypt', 'sign', 'verify'],
  });

  const aes = new AES(k, 'aes-256-cbc');
  const e = await aes.encrypt(data);

  if(!encoding) return e.buffer;
  return e.toString(encoding);
}

export async function aesDecrypt<T = any>(data: string | Buffer | Uint8Array): Promise<Decrypted<T>> {
  if(!process.env.SYMMETRIC_ENC_KEY) {
    throw new Error('[SYMMETRIC_ENC_KEY] Unset encryption key');
  }
  
  const k = new SymmetricKey(Buffer.from(env.getEnvironmentVariable('SYMMETRIC_ENC_KEY')!), {
    algorithm: 'aes-256-cbc',
    usages: ['encrypt', 'decrypt', 'sign', 'verify'],
  });

  const aes = new AES(k, 'aes-256-cbc');
  return aes.decrypt(data instanceof Buffer ? data : Buffer.from(data));
}
