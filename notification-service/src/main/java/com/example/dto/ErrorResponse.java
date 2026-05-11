package com.example.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int statusCode,
        String status,
        String message,
        String path) {
}
