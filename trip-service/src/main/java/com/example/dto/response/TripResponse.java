package com.example.dto.response;

import com.example.enums.TripStatus;

public record TripResponse(
        Long id,
        Long passengerId,
        Long driverId,
        TripStatus status,
        String origin,
        String destination,
        Integer price,
        Integer rating) {
}
