package com.pedroheing.shoppingcart.common.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    public abstract HttpStatus status();
    public abstract String errorCode();

    public Map<String, Object> details() {
        return Map.of();
    }
}