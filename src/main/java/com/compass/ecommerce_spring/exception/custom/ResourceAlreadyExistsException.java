package com.compass.ecommerce_spring.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String msg) {
        super(msg);
    }

    public ResourceAlreadyExistsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
