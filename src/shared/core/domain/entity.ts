import { Crypto } from 'typesdk/crypto';

import { shortId, uuid, uuidWithoutSlashes } from '@shared/lib/id';


export abstract class Entity<T> {
  protected readonly _id: string;
  protected readonly props: T;

  get id() {
    return this._id;
  }

  constructor(props: T, id?: string) {
    this._id = id ?? Crypto.uuid().replace(/-/g, '');  
    this.props = props;
  }

  public static generateId(type: 'short' | 'uuid' | 'uuid-without-slashes'): string {
    switch(type) {
      case 'uuid':
        return uuid();
      case 'uuid-without-slashes':
        return uuidWithoutSlashes();
      case 'short':
      default:
        return shortId();
    }
  }

  public equals(object?: Entity<T>): boolean {
    if(object === null || object === undefined) return false;

    if(this === object) return true;

    if(!(object instanceof Entity)) return false;

    return this._id === object._id;
  }
}

export default Entity;
