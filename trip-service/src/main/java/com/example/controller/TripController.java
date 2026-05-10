package com.example.controller;

import com.example.dto.request.CreateTripRequest;
import com.example.dto.request.RateTripRequest;
import com.example.dto.request.UpdateTripStatusRequest;
import com.example.dto.response.TripResponse;
import com.example.service.TripService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @GetMapping("/{id}")
    public TripResponse getTripById(@PathVariable Long id) {
        return tripService.getTripById(id);
    }

    @GetMapping()
    public List<TripResponse> getTripsByPassengerId(@RequestParam("passenger_id") Long passengerId) {
        return tripService.getTripsByPassengerId(passengerId);
    }

    @PostMapping
    public TripResponse createTrip(@Valid @RequestBody CreateTripRequest requestBody) {
        return tripService.createTrip(requestBody);
    }

    @PatchMapping("/{id}/status")
    public TripResponse updateTripStatusById(@PathVariable Long id,
                                             @Valid @RequestBody UpdateTripStatusRequest requestBody) {
        return tripService.updateTripStatusById(id, requestBody);
    }

    @PostMapping("/{id}/rate")
    public TripResponse rateTripById(@PathVariable Long id,
                                     @Valid @RequestBody RateTripRequest requestBody) {
        return tripService.rateTrip(id, requestBody.rating());
    }
}
