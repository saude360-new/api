import cors from 'cors';
import * as http from 'http';
import { Server } from 'socket.io';

import v1Router from './routes';
import app, { port } from './app';
import env from '@shared/infra/env';



const srv = http.createServer(app);

srv.on('listening', onListening);
srv.on('error', onError);


async function onListening() {
  if(!process.env.VERCEL_ENV) {
    // log:: `${format.colors.magenta}[NodeJS.Server]${format.reset} Listening on: ${format.colors.green}http://127.0.0.1:${port}\n`
  }

  app.use('/api/v1',
    cors({
      credentials: true,
      origin: env.getEnvironmentVariable('CORS_ORIGIN', { fallback: '*' }).split(',').map(item => item.trim()) ?? undefined,
      // exposedHeaders: ['x-total-count', 'Content-Type', 'Content-Length'],
    }),
    // parseEncryptedBody,
    v1Router);

  const wss = new Server({
    cors: {
      origin: '*',
    },
  });

  wss.listen(srv);
}

function onError(e: Error) {
  console.error(e);
  process.exit(1);
}


if(!process.env.VERCEL_ENV) {
  srv.listen(port);
}

export default srv;
