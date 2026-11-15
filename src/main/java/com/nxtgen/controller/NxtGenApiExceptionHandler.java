package com.nxtgen.controller;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NxtGenApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NxtGenApiExceptionHandler.class);

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<Map<String, String>> handleAuthenticationException(
            AuthenticationException exception,
            HttpServletRequest request) {
        LOGGER.error("Authentication failed for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Your email or password is incorrect."));
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Map<String, String>> handleAccessDeniedException(
            AccessDeniedException exception,
            HttpServletRequest request) {
        LOGGER.error("Access denied for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "You do not have permission to access this resource."));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, String>> handleException(Exception exception, HttpServletRequest request) {
        LOGGER.error("API request failed for {} {}", request.getMethod(), request.getRequestURI(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "We could not complete your request. Please try again."));
    }
}
