package com.seahere.backend.common.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class SeaHereException extends RuntimeException {
    private final Map<String,String> validation = new HashMap<>();

    public SeaHereException(String message) {
        super(message);
    }

    public SeaHereException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String value) {
        validation.put(fieldName,value);
    }
}