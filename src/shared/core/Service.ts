export interface Service<Request, Response> {
  execute(request: Request): Promise<Response> | Response;
}

export default Service;
