package com.example.dto.request;

import com.example.enums.RecipientType;
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
        String message) {
}
