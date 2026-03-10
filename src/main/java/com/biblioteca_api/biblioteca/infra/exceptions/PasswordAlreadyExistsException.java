package com.biblioteca_api.biblioteca.infra.exceptions;

public class PasswordAlreadyExistsException extends RuntimeException {
    public PasswordAlreadyExistsException(String message) {
        super(message);
    }
}
