package com.example.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTripRequest(
        @NotNull(message = "passenger id is required")
        Long passengerId,

        @NotBlank(message = "origin is required")
        String origin,

        @NotBlank(message = "destination is required")
        String destination,

        @NotNull(message = "distance is required")
        @Min(50)
        Integer distance,

        @NotNull(message = "tariff is required")
        @Min(1)
        Integer tariff) {
}
