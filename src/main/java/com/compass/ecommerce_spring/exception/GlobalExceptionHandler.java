package com.compass.ecommerce_spring.exception;

import com.compass.ecommerce_spring.exception.custom.BusinessException;
import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        var errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return processResponseEntity(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardError> handleBusinessException(BusinessException ex, WebRequest request) {
        var errors = Collections.singletonList(ex.getMessage());
        return processResponseEntity(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        var errors = Collections.singletonList(ex.getMessage());
        return processResponseEntity(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler({AccessDeniedException.class, ExpiredJwtException.class, MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<StandardError> handleSecurityException(Exception ex, WebRequest request) {
        var errors = Collections.singletonList(ex.getMessage());
        return processResponseEntity(HttpStatus.FORBIDDEN, errors, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        var errors = Collections.singletonList(ex.getMessage());
        return processResponseEntity(HttpStatus.UNAUTHORIZED, errors, request);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<StandardError> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        var errors = Collections.singletonList(ex.getMessage());
        return processResponseEntity(HttpStatus.CONFLICT, errors, request);
    }

    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<StandardError> handleResourceNotFoundException(Exception ex, WebRequest request) {
        var errors = Collections.singletonList(ex.getMessage());
        return processResponseEntity(HttpStatus.NOT_FOUND, errors, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception ex, WebRequest request) {
        var errors = Collections.singletonList(ex.getMessage());
        return processResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, errors, request);
    }

    private ResponseEntity<StandardError> processResponseEntity(HttpStatus status, List<String> errors, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(LocalDateTime.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }
}
