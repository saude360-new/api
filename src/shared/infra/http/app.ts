import env from '@shared/infra/env';

import express from 'express';
import cookieParser from 'cookie-parser';
import type { Writable } from 'typesdk/types';

import * as inet from '@shared/core/inet';
// import type { UserDocument } from '@models/users';
import { uuidWithoutSlashes } from '@shared/lib/id';
// import type { SessionDocument } from '@models/sessions';



declare global {
  // eslint-disable-next-line @typescript-eslint/no-namespace
  namespace Express {
    interface Request {
      readonly requestId: string;
      readonly decryptedBody?: Readonly<Record<string, any> & { __$payload?: any; }>;

      readonly inet: {
        readonly ip: inet.IPv4 | inet.IPv6
      };

      readonly context?: {
        [key: string]: any;

        // readonly session?: SessionDocument<{ userId: string }>;
        // readonly user?: UserDocument;
      };
    }
  }
}

const app = express();
export const port = parseInt(env.getEnvironmentVariable('PORT', { fallback: '7356' }), 10);

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.disable('x-powered-by');

if(!env.isProduction()) {
  app.set('port', port);
}


app.use(async (req, res, next) => {
  function qs() {
    if(!req.query) return '';

    const query = Object.entries(req.query).map(([key, value]) => `${key}=${value}`).join('&');
    return query && query.trim().length > 0 ? `?${query}` : '';
  }

  (req as Writable<typeof req>).requestId = uuidWithoutSlashes();
  
  (req as Writable<typeof req>).inet = {
    ip: inet.extractIPFromRequest(req),
  };

  const abspath = req.path.split('?')[0].trim();

  if(abspath.endsWith('/') && abspath !== '/') return res.redirect(
    301,
    abspath.slice(0, -1) + qs() // eslint-disable-line comma-dangle
  );

  next();
});


export default app;
