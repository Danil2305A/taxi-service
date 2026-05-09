package com.example.controller;

import com.example.dto.request.RegisterDriverRequest;
import com.example.dto.request.UpdateDriverStatusRequest;
import com.example.dto.response.DriverResponse;
import com.example.entity.Driver;
import com.example.mapper.DriverMapper;
import com.example.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    @PostMapping
    public DriverResponse registerDriver(@Valid @RequestBody RegisterDriverRequest requestBody) {
        Driver newDriver = driverMapper.toEntity(requestBody);
        Driver registeredDriver = driverService.createDriver(newDriver);
        return driverMapper.toResponse(registeredDriver);
    }

    @GetMapping("/{id}")
    public DriverResponse getDriverById(Long id) {
        Driver foundedDriver = driverService.getDriverById(id);
        return driverMapper.toResponse(foundedDriver);
    }

    @PatchMapping("/{id}/status")
    public DriverResponse updateDriverStatusById(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateDriverStatusRequest requestBody) {
        Driver updatedDriver = driverService.updateDriverStatusById(id, requestBody.status());
        return driverMapper.toResponse(updatedDriver);
    }

    @PostMapping("/assign")
    public DriverResponse assignDriver() {
        Driver assignedDriver = driverService.assignAvailableDriver();
        return driverMapper.toResponse(assignedDriver);
    }
}
