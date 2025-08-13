package com.smarthealth.io.smarthealth.controllers;

import com.smarthealth.io.smarthealth.dtos.ErrorResponseDto;
import com.smarthealth.io.smarthealth.exceptions.InvalidCredentialsException;
import com.smarthealth.io.smarthealth.exceptions.ResourceNotFoundException;
import com.smarthealth.io.smarthealth.exceptions.UserAlreadyExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Testes unitários para GlobalExceptionHandler.
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        when(webRequest.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void handleResourceNotFoundException_ShouldReturnNotFound() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Usuário", "123");

        // Act
        ResponseEntity<ErrorResponseDto> response = globalExceptionHandler
                .handleResourceNotFoundException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Recurso Não Encontrado", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Usuário não encontrado"));
        assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void handleUserAlreadyExistsException_ShouldReturnConflict() {
        // Arrange
        UserAlreadyExistsException exception = new UserAlreadyExistsException("test@example.com");

        // Act
        ResponseEntity<ErrorResponseDto> response = globalExceptionHandler
                .handleUserAlreadyExistsException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().getStatus());
        assertEquals("Conflito", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("test@example.com"));
        assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void handleInvalidCredentialsException_ShouldReturnUnauthorized() {
        // Arrange
        InvalidCredentialsException exception = new InvalidCredentialsException();

        // Act
        ResponseEntity<ErrorResponseDto> response = globalExceptionHandler
                .handleInvalidCredentialsException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getStatus());
        assertEquals("Não Autorizado", response.getBody().getError());
        assertEquals("Credenciais inválidas", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
    }

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() {
        // Arrange
        FieldError fieldError1 = new FieldError("userDto", "email", "não pode estar vazio");
        FieldError fieldError2 = new FieldError("userDto", "password", "deve ter pelo menos 8 caracteres");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Act
        ResponseEntity<ErrorResponseDto> response = globalExceptionHandler
                .handleValidationExceptions(methodArgumentNotValidException, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Erro de Validação", response.getBody().getError());
        assertEquals("Os dados fornecidos são inválidos", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
        
        assertNotNull(response.getBody().getValidationErrors());
        assertEquals(2, response.getBody().getValidationErrors().size());
        
        ErrorResponseDto.ValidationErrorDto validationError1 = response.getBody().getValidationErrors().get(0);
        assertEquals("email", validationError1.getField());
        assertEquals("não pode estar vazio", validationError1.getMessage());
        
        ErrorResponseDto.ValidationErrorDto validationError2 = response.getBody().getValidationErrors().get(1);
        assertEquals("password", validationError2.getField());
        assertEquals("deve ter pelo menos 8 caracteres", validationError2.getMessage());
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() {
        // Arrange
        Exception exception = new RuntimeException("Erro interno");

        // Act
        ResponseEntity<ErrorResponseDto> response = globalExceptionHandler
                .handleGenericException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Erro Interno do Servidor", response.getBody().getError());
        assertEquals("Ocorreu um erro interno. Tente novamente mais tarde.", response.getBody().getMessage());
        assertEquals("/test", response.getBody().getPath());
    }
}

