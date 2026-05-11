package com.example.exception;

public class NoAvailableDriversException extends RuntimeException {
    public NoAvailableDriversException(String message) {
        super(message);
    }

    public NoAvailableDriversException() {
        super("No available drivers at the moment");
    }
}
