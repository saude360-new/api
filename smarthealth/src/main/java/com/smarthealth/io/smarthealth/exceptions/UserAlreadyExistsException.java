package com.smarthealth.io.smarthealth.exceptions;

/**
 * Exceção lançada quando se tenta criar um usuário que já existe.
 */
public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
