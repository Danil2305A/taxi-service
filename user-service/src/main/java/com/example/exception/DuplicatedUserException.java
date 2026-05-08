package com.example.exception;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException(String message) {
        super(message);
    }

    public DuplicatedUserException(String userType, String field, String value) {
        super(String.format("%s already exists with %s: %s", userType, field, value));
    }
}
