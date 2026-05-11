package com.example.client;

import com.example.dto.response.DriverResponse;
import com.example.dto.response.PassengerResponse;
import com.example.enums.DriverStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;

    public Optional<PassengerResponse> getPassengerById(Long id) {
        try {
            PassengerResponse passenger = restTemplate.getForObject(
                    userServiceUrl + "/api/passengers/{id}",
                    PassengerResponse.class, id);
            return Optional.ofNullable(passenger);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public DriverResponse assignDriver() {
        return restTemplate.postForObject(
                userServiceUrl + "/api/drivers/assign",
                null, DriverResponse.class);
    }

    public void updateDriverStatusById(Long id, DriverStatus status) {
        Map<String, String> requestBody = Map.of("status", status.name());
        restTemplate.patchForObject(
                userServiceUrl + "/api/drivers/{id}/status",
                requestBody, Void.class, id);
    }
}
