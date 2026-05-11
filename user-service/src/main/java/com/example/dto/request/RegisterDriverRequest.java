package com.example.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterDriverRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, message = "Name length must be at least 2 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^79\\d{9}$", message = "Invalid phone number (ex. 79005553535)")
        String phone,

        @NotBlank(message = "License number is required")
        String licenseNumber,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password length must be at least 8 characters")
        String password) {
}
