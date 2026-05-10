package com.example.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RateTripRequest(
        @NotNull(message = "rating is required")
        @Min(1)
        @Max(5)
        Integer rating) {
}
