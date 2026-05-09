package com.example.dto;

import com.example.enums.RecipientType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateNotificationTaskRequest(
        @NotNull(message = "trip id is required")
        Long tripId,

        @NotNull(message = "recipient type is required")
        RecipientType recipientType,

        @NotNull(message = "recipient id is required")
        Long recipientId,

        @NotBlank(message = "message is required")
        String message,

        @NotNull(message = "attempts is required")
        @Min(2)
        @Max(10)
        Integer attempts) {
}
