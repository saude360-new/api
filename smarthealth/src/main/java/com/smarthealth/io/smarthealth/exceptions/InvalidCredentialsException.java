package com.smarthealth.io.smarthealth.exceptions;

/**
 * Exceção lançada quando as credenciais fornecidas são inválidas.
 */
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public InvalidCredentialsException() {
        super("Credenciais inválidas");
    }
}

