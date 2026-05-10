package com.example.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String userType, Long id) {
        super(String.format("%s not found with id: %d", userType, id));
    }
}
