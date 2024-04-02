import type { Dict } from 'typesdk/types';
import { Crypto } from 'typesdk/crypto';


const aliases: Dict<string> = {
  '%': ' por cento',
  '>': '-',
  '<': '-',
  '@': '-',
  '.': '-',
  ',': '-',
  '&': ' e ',
  _: '-',
  '/': '-',
};


export function slugfy(title?: string, ignore?: string[]): string {
  if(!title || title.trim().length < 6) return Crypto.uuid().replace(/-/g, '');
  let slug = title.toLowerCase();

  for(const alias in aliases) {
    if(!Object.prototype.hasOwnProperty.call(aliases, alias)) continue;
    if(!!ignore && Array.isArray(ignore) && ignore.includes(alias)) continue;
    
    slug = slug.replaceAll(alias, aliases[alias]);
  }

  slug = slug.replace(/\s+/g, '-');
  return slug.trim();
}

export default slugfy;
