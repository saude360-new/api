export interface DomainError {
  readonly message: string;
  statusCode?: number;
  location?: string;
}

export default DomainError;
