package com.compass.ecommerce_spring.exception;

import com.compass.ecommerce_spring.exception.custom.BusinessException;
import com.compass.ecommerce_spring.exception.custom.InvalidTokenException;
import com.compass.ecommerce_spring.exception.custom.ResourceAlreadyExistsException;
import com.compass.ecommerce_spring.exception.custom.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class,
            BusinessException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            PropertyReferenceException.class,
            IllegalArgumentException.class})
    public ResponseEntity<StandardError> handleBadRequestException(Exception ex, WebRequest request) {
        var errors = new ArrayList<String>();

        if (ex instanceof MethodArgumentNotValidException) {
            errors.addAll(processMethodArgumentNotValidException((MethodArgumentNotValidException) ex));
        } else {
            errors.add(ex.getMessage());
        }

        return processResponseEntity(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler({AccessDeniedException.class,
            ExpiredJwtException.class,
            MalformedJwtException.class,
            SignatureException.class,
            DisabledException.class,
            InvalidTokenException.class})
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

    private List<String> processMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
    }

    private ResponseEntity<StandardError> processResponseEntity(HttpStatus status, List<String> errors, WebRequest request) {
        String path = request.getDescription(false).replace("uri=", "");
        StandardError standardError = new StandardError(LocalDateTime.now(), status.value(), errors, path);
        return ResponseEntity.status(status).body(standardError);
    }
}
