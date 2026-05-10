package com.example.client;

import com.example.dto.request.CreateNotificationTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NotificationServiceClient {
    private final RestTemplate restTemplate;

    @Value("${notification-service.url}")
    private String notificationServiceUrl;

    public void sendNotificationTask(CreateNotificationTaskRequest request) {
        restTemplate.postForObject(notificationServiceUrl + "/api/notifications", request, Void.class);
    }
}
