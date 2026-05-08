package com.example.exception;

public class InvalidUserCredentialsException extends RuntimeException {
    public InvalidUserCredentialsException(String message) {
        super(message);
    }

    public InvalidUserCredentialsException() {
        super("Invalid user credentials");
    }
}
