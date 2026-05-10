package com.example.exception;

public class IllegalTripStateException extends RuntimeException {
    public IllegalTripStateException(String message) {
        super(message);
    }
}
