package com.example.dto.response;

import com.example.enums.DriverStatus;

public record DriverResponse(
        Long id,
        String name,
        String email,
        String phone,
        String licenseNumber,
        DriverStatus status) {
}
