package com.walletservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedExceptions(UnauthorizedException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
    }
    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<Map<String, Object>> handleMissingFieldException(MissingFieldException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundExceptions(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "NOT FOUND", ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String errorMessage = "Endpoint not found: " + ex.getRequestURL();
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", Optional.of(status.value()));
        response.put("error", error);
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}
