package com.example.service;

import com.example.dto.request.LoginRequest;
import com.example.dto.response.LoginResponse;
import com.example.entity.Driver;
import com.example.entity.Passenger;
import com.example.enums.UserRole;
import com.example.exception.InvalidUserCredentialsException;
import com.example.repository.DriverRepository;
import com.example.repository.PassengerRepository;
import com.example.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final PassengerRepository passengerRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest requestBody) {
        UserRole role = requestBody.role();
        String email = requestBody.email();
        String password = requestBody.password();

        if (role.equals(UserRole.PASSENGER)) {
            Passenger passenger = passengerRepository.findByEmail(email)
                    .orElseThrow(InvalidUserCredentialsException::new);

            if (!passwordEncoder.matches(password, passenger.getPasswordHash())) {
                throw new InvalidUserCredentialsException();
            }

            String token = jwtProvider.generateToken(passenger.getId().toString(), UserRole.PASSENGER.name());

            return new LoginResponse(token);
        }

        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(InvalidUserCredentialsException::new);

        if (!passwordEncoder.matches(password, driver.getPasswordHash())) {
            throw new InvalidUserCredentialsException();
        }

        String token = jwtProvider.generateToken(driver.getId().toString(), UserRole.DRIVER.name());

        return new LoginResponse(token);
    }
}
