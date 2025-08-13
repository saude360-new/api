package com.smarthealth.io.smarthealth.dtos;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respostas de erro padronizadas da API.
 */
public class ErrorResponseDto {
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<ValidationErrorDto> validationErrors;
    
    public ErrorResponseDto() {
        this.timestamp = LocalDateTime.now();
    }
    
    public ErrorResponseDto(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
    
    // Getters e Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public List<ValidationErrorDto> getValidationErrors() {
        return validationErrors;
    }
    
    public void setValidationErrors(List<ValidationErrorDto> validationErrors) {
        this.validationErrors = validationErrors;
    }
    
    /**
     * DTO para erros de validação específicos.
     */
    public static class ValidationErrorDto {
        private String field;
        private Object rejectedValue;
        private String message;
        
        public ValidationErrorDto(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }
        
        // Getters e Setters
        public String getField() {
            return field;
        }
        
        public void setField(String field) {
            this.field = field;
        }
        
        public Object getRejectedValue() {
            return rejectedValue;
        }
        
        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}

