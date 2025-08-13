package com.smarthealth.io.smarthealth.exceptions;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s não encontrado com identificador: %s", resourceType, identifier));
    }
}

