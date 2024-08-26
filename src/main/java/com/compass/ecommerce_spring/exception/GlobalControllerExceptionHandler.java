package com.compass.ecommerce_spring.exception;

import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(Instant.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler({AccessDeniedException.class, ExpiredJwtException.class, MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<StandardError> handleSecurityException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        List<String> errors = Collections.singletonList(ex.getMessage());
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(Instant.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        List<String> errors = Collections.singletonList(ex.getMessage());
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(Instant.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<StandardError> handleUserAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        List<String> errors = Collections.singletonList(ex.getMessage());
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(Instant.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<StandardError> handleResourceNotFoundException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        List<String> errors = Collections.singletonList(ex.getMessage());
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(Instant.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> handleException(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        List<String> errors = Collections.singletonList(ex.getMessage());
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(Instant.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }
}
