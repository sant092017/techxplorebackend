package com.techxplore.techxplorebackend.advice;

import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        String msg = ex.getConstraintViolations().stream()
                .map(cv -> cv.getPropertyPath() + " : " + cv.getMessage())
                .collect(Collectors.joining("; "));
        logger.warn("Validation failed: {}", msg);
        ErrorResponse resp = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", msg, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + " : " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.warn("Method argument validation failed: {}", msg);
        ErrorResponse resp = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", msg, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String required = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        String msg = ex.getName() + " should be of type " + required;
        logger.warn("Type mismatch: {}", msg);
        ErrorResponse resp = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", msg, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception: ", ex);
        // TODO: Remove detailed message before production
        String detailMsg = ex.getClass().getSimpleName() + ": " + ex.getMessage()
                + (ex.getCause() != null ? " | Cause: " + ex.getCause().getMessage() : "");
        ErrorResponse resp = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", detailMsg, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
    }
}

