package com.github.alef_torres.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedmathOperationException extends RuntimeException {
    public UnsupportedmathOperationException(String message) {
        super(message);
    }
}
