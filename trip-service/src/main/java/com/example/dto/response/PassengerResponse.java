package com.example.dto.response;

public record PassengerResponse(
        Long id,
        String name,
        String email,
        String phone) {
}
