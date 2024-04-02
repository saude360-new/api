import { Request, Response } from 'express';
import { jsonSafeStringify } from 'typesdk/safe-json';
import { ExtendedSerializableError } from 'typesdk/errors/http/extended';

// import inet from '@shared/core/inet';
// import writeLog from '@shared/core/log';
import DomainError from '@shared/core/domain/errors';


export async function handleRouteError(context: DomainError, request: Request, response: Response): Promise<void>;
export async function handleRouteError(context: { [key: string]: any }, request: Request, response: Response): Promise<void>;
export async function handleRouteError(context: object, request: Request, response: Response): Promise<void>;
export async function handleRouteError(context: null, request: Request, response: Response): Promise<void>;
export async function handleRouteError(context: undefined, request: Request, response: Response): Promise<void>;
export async function handleRouteError(
  context: any,
  _: Request,
  response: Response // eslint-disable-line comma-dangle
): Promise<void> {
  if(!context) {
    console.error('handleRouteError was called with an undefined context');
  }

  if(!context) return void response.writeHead(500).end();
  response.setHeader('Content-Type', 'application/json');

  const s = context.statusCode ?? 500;
  response.status(s);

  if(s >= 500) {
    // await writeLog('stderr', `${context.message} [from=${inet.extractIPFromRequest(request).address} contextual-session-id=${request.context.session ? request.context.session.sessionId : 'undefined'} contextual-user-id=${request.context.user ? request.context.user.userId : 'undefined'}] at ${context.stack}`);
    console.error(context);
  } else {
    // console.warn(context);
  }

  if(context instanceof ExtendedSerializableError) {
    response.send(jsonSafeStringify(context.serialize()));
    return void response.end();
  } else {
    response.send(jsonSafeStringify({
      action: context.action ?? 'Check the server logs for more information.',
      context: context.context ?? {},
      message: context.message,
      _raw: context,
    }));

    return void response.end();
  }
}
