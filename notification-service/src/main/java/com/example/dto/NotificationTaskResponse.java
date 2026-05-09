package com.example.dto;

import com.example.enums.NotificationTaskStatus;
import com.example.enums.RecipientType;

public record NotificationTaskResponse(
        Long id,
        Long tripId,
        RecipientType recipientType,
        Long recipientId,
        String message,
        NotificationTaskStatus status,
        Integer attempts) {
}
