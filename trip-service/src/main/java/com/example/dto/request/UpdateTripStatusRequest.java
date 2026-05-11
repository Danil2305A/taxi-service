package com.example.dto.request;

import com.example.enums.TripStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTripStatusRequest(@NotNull TripStatus status) {
}
