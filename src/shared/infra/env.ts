import path from 'node:path';
import dotenv from 'typesdk/dotenv';
import { Exception } from 'typesdk/errors';
import { isThenable } from 'not-synchronous/core';
import type { MaybePromise } from 'typesdk/types';
import { validateEmail } from 'typesdk/validators';
import { assertString } from 'typesdk/utils/assertions';

import { UUIDPattern } from '@shared/lib/id';
import { ensureDirSync } from '@shared/lib/fs';


dotenv.load();


export interface MappedEnvironmentVariables {
  //
}

export interface EnvironmentVariables extends MappedEnvironmentVariables {
  [key: string]: string;
}


export type VariableMap = {
  [key: string]: {
    type: 'string' | 'number' | 'boolean';
    required: boolean;
    default?: string | number | boolean;
    validate?: 'uuid' | 'uuid-without-slashes' | 'cid' | 'url' | 'email';
    oneOf?: readonly string[];
  }
}

export interface VariableResolverContext {
  getAppRoot(): string | undefined;
  getExecPath(): string | undefined;
  getEnvironmentVariables(): MaybePromise<EnvironmentVariables>;
}

export type FetchEnvOptions<T> = {
  fallback?: string;
  strict?: boolean;
  rewrite?: (value: string) => T;
}

export class AbstractVariableResolverService {
  public static readonly VARIABLE_LHS = '${';
  public static readonly VARIABLE_REGEXP = /\$\{(.*?)\}/g;

  private _requiredVariables?: VariableMap;
  // private readonly _context?: VariableResolverContext;
  private readonly _environmentVariables?: EnvironmentVariables;

  public constructor(
    _context?: VariableResolverContext,
    _environmentVariables?: EnvironmentVariables // eslint-disable-line comma-dangle
  ) {
    // eslint-disable-next-line no-extra-boolean-cast
    if(!!_environmentVariables) {
      this._environmentVariables = _environmentVariables;
    }

    // eslint-disable-next-line no-extra-boolean-cast
    if(!!_context) {
      const envPromise = _context.getEnvironmentVariables();

      if(isThenable(envPromise)) {
        throw new Exception('VariableResolverService: Cannot handle asynchronous environment variables');
      }

      this._environmentVariables = envPromise;
    }
  }

  public getTempPath(): string {
    const p = path.join(process.cwd(), 'tmp');
    ensureDirSync(p);

    return p;
  }

  public getStaticPath(): string {
    const p = path.join(process.cwd(), 'static');
    return p;
  }

  public setRequired(variables: VariableMap): void {
    this._requiredVariables ??= {};
    Object.assign(this._requiredVariables, variables);
  }

  public assertAllSet(): void {
    if(!this._requiredVariables) return;

    for(const prop in this._requiredVariables) {
      if(!Object.prototype.hasOwnProperty.call(this._requiredVariables, prop)) continue;

      if(!Object.prototype.hasOwnProperty.call(this._environmentVariables, prop)) {
        throw new Exception(`VariableResolverService: Environment variable '${prop}' is not defined`);
      }

      switch(this._requiredVariables[prop].type) {
        case 'number':
          if(isNaN(Number(this._environmentVariables?.[prop]))) {
            throw new Exception(`VariableResolverService: Environment variable '${prop}' is not a number`);
          }
          break;
        case 'boolean':
          if(this._environmentVariables?.[prop] !== 'true' &&
            this._environmentVariables?.[prop] !== 'false') {
            throw new Exception(`VariableResolverService: Environment variable '${prop}' is not a boolean`);
          }
          break;
      }

      switch(this._requiredVariables[prop].validate) {
        case 'url':
          try {
            new URL(this._environmentVariables?.[prop] ?? '');
          } catch(e) {
            throw new Exception(`VariableResolverService: Environment variable '${prop}' is not a valid URL`);
          }
          break;
        case 'uuid':
          if(!UUIDPattern.test(this._environmentVariables![prop])) {
            throw new Exception(`VariableResolverService: Environment variable '${prop}' is not a valid UUID`);
          }
          break;
        case 'uuid-without-slashes':
          if(!/^[0-9a-f]{32}$/i.test(this._environmentVariables![prop])) {
            throw new Exception(`VariableResolverService: Environment variable '${prop}' is not a valid UUID`);
          }
          break;
        case 'email':
          if(!validateEmail(this._environmentVariables![prop])) {
            throw new Exception(`VariableResolverService: Environment variable '${prop}' is not a valid email`);
          }
      }

      if(!!this._requiredVariables[prop].oneOf &&
        Array.isArray(this._requiredVariables[prop].oneOf) &&
        !this._requiredVariables[prop].oneOf!.includes(this._environmentVariables![prop])) {
        throw new Exception(`VariableResolverService: Environment variable '${prop}' is not one of these allowed values: ${this._requiredVariables[prop].oneOf!.join(', ')}`);
      }
    }
  }

  public getEnvironmentVariable<K extends keyof MappedEnvironmentVariables, T = string>(name: K | Omit<string, K>, options: Omit<FetchEnvOptions<T>, 'fallback'> & { fallback: string }): T;
  public getEnvironmentVariable<K extends keyof MappedEnvironmentVariables, T = string>(name: K | Omit<string, K>, options?: Omit<FetchEnvOptions<T>, 'fallback'> & { fallback?: undefined }): T | null;
  public getEnvironmentVariable<K extends keyof MappedEnvironmentVariables, T = string>(name: K | Omit<string, K>, options?: FetchEnvOptions<T>): T | null {
    assertString(name);

    if(!this._environmentVariables) return null;
    let env: string | T | null | undefined = this._environmentVariables[name] ?? options?.fallback;

    if(!env && options?.strict === true) {
      throw new Exception(`VariableResolverService: Environment variable '${name}' is not defined`);
    }

    if(!env) return null;

    if(!!options?.rewrite && 
      typeof options.rewrite === 'function') {
      if(isThenable(options.rewrite)) {
        throw new Exception('VariableResolverService: Cannot handle asynchronous rewrite function for environment variables');
      }

      env = options.rewrite(env);
    }

    return env as T;
  }

  public setEnvironmentVariable(name: string, value: string, override: boolean = false): void {
    if(!this._environmentVariables) return;

    if(!override &&
      typeof this._environmentVariables[name] !== 'undefined' &&
      !!this._environmentVariables[name]) return;

    this._environmentVariables[name] = value;
  }

  public is(env: 'development' | 'test' | 'production' | Omit<string, 'development' | 'test' | 'production'>): boolean {
    const e = this.getEnvironmentVariable('NODE_ENV', { fallback: 'development' });
    return e === env;
  }

  public isDevelopment(): boolean {
    return this.is('development');
  }

  public isProduction(): boolean {
    return this.is('production');
  }

  public isTest(): boolean {
    return this.is('test');
  }

  public env(): EnvironmentVariables {
    // eslint-disable-next-line no-extra-boolean-cast
    if(!!this._requiredVariables) {
      this.assertAllSet();
    }

    return { ...this._environmentVariables } as EnvironmentVariables;
  }
}


const requiredEnvironmentVariables: VariableMap = {} as const;

const env = new AbstractVariableResolverService(undefined, process.env as EnvironmentVariables);
env.setRequired(requiredEnvironmentVariables);
env.assertAllSet();

export default env;
