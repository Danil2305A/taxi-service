package com.example.controller;

import com.example.dto.request.RegisterPassengerRequest;
import com.example.dto.response.PassengerResponse;
import com.example.entity.Passenger;
import com.example.mapper.PassengerMapper;
import com.example.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passengers")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    @PostMapping
    public PassengerResponse registerPassenger(@Valid @RequestBody RegisterPassengerRequest requestBody) {
        Passenger newPassenger = passengerMapper.toEntity(requestBody);
        Passenger registeredPassenger = passengerService.createPassenger(newPassenger);
        return passengerMapper.toResponse(registeredPassenger);
    }

    @GetMapping("/{id}")
    public PassengerResponse getPassengerById(@PathVariable Long id) {
        Passenger foundedPassenger = passengerService.getPassengerById(id);
        return passengerMapper.toResponse(foundedPassenger);
    }
}
