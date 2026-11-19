package com.WF360.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import com.WF360.services.WF360BankDetailUpdateWindowException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Centralised (global) exception handling for every {@code @RestController} in the application.
 * Every response body is a flat {@code {"message": "..."}} JSON object (the shape every frontend
 * API wrapper already expects), optionally with an {@code "errors"} map of per-field validation
 * messages. Internal exception details (stack traces, SQL text, class names) are logged here via
 * SLF4J but are never put in the response body, so clients only ever see safe, user-facing text.
 */
@RestControllerAdvice
public class WF360ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WF360ApiExceptionHandler.class);

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

    @ExceptionHandler(WF360BankDetailUpdateWindowException.class)
    ResponseEntity<Map<String, String>> handleBankDetailUpdateWindow(
            WF360BankDetailUpdateWindowException exception,
            HttpServletRequest request) {
        LOGGER.warn("Bank detail update blocked for {} {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {
        LOGGER.warn("Invalid request for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", exception.getMessage()));
    }

    /** Thrown when a {@code @Valid @RequestBody} fails bean-validation (e.g. a blank mandatory field). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }
        LOGGER.warn(
                "Validation failed for {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                fieldErrors);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Please correct the highlighted fields and try again.");
        body.put("errors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** Thrown when request parameters/path variables fail {@code @Validated} constraints (not request bodies). */
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<Map<String, String>> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        LOGGER.warn("Constraint violation for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "Please correct the submitted request and try again."));
    }

    /** Request body is missing, empty, or not valid JSON. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<Map<String, String>> handleMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        LOGGER.warn("Unreadable request body for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "The request could not be read. Please check the submitted data."));
    }

    /** A required query/form parameter was not supplied. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    ResponseEntity<Map<String, String>> handleMissingParameter(
            MissingServletRequestParameterException exception,
            HttpServletRequest request) {
        LOGGER.warn("Missing parameter for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "A required field is missing from the request."));
    }

    /** A path variable/parameter could not be converted to its expected type (e.g. text where a number was expected). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<Map<String, String>> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request) {
        LOGGER.warn("Type mismatch for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", "One or more values in the request were not in the expected format."));
    }

    /** The HTTP method used isn't supported by the matched route (e.g. DELETE on a GET-only endpoint). */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<Map<String, String>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException exception,
            HttpServletRequest request) {
        LOGGER.warn("Method not supported for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(Map.of("message", "This operation is not supported for the requested resource."));
    }

    /** No controller route matches the requested path. */
    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<Map<String, String>> handleNoResourceFound(
            NoResourceFoundException exception,
            HttpServletRequest request) {
        LOGGER.warn("No route found for {} {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", "The requested resource could not be found."));
    }

    /** Uploaded file (e.g. a profile image) exceeds the configured size limit. */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<Map<String, String>> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException exception,
            HttpServletRequest request) {
        LOGGER.warn("Upload too large for {} {}: {}", request.getMethod(), request.getRequestURI(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("message", "The uploaded file is too large. Please choose a smaller file."));
    }

    /** A write conflicted with a database constraint (e.g. a duplicate unique value). */
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<Map<String, String>> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request) {
        LOGGER.error("Data integrity violation for {} {}", request.getMethod(), request.getRequestURI(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("message", "This request conflicts with existing data and could not be completed."));
    }

    /** Catch-all safety net: any other unexpected failure becomes a generic 500 with no internal detail leaked. */
    @ExceptionHandler(Exception.class)
    ResponseEntity<Map<String, String>> handleException(Exception exception, HttpServletRequest request) {
        LOGGER.error("API request failed for {} {}", request.getMethod(), request.getRequestURI(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "We could not complete your request. Please try again."));
    }
}
