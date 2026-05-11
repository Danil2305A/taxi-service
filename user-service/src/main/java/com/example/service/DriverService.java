package com.example.service;

import com.example.entity.Driver;
import com.example.enums.DriverStatus;
import com.example.exception.DuplicatedUserException;
import com.example.exception.NoAvailableDriversException;
import com.example.exception.UserNotFoundException;
import com.example.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DriverService {
    private final DriverRepository driverRepository;

    @Transactional
    public Driver createDriver(Driver driver) {
        String email = driver.getEmail();
        if (driverRepository.existsByEmail(email)) {
            throw new DuplicatedUserException("Driver", "email", email);
        }

        String phone = driver.getPhone();
        if (driverRepository.existsByPhone(phone)) {
            throw new DuplicatedUserException("Driver", "phone", phone);
        }

        String licenseNumber = driver.getLicenseNumber();
        if (driverRepository.existsByLicenseNumber(licenseNumber)) {
            throw new DuplicatedUserException("Driver", "license number", licenseNumber);
        }

        return driverRepository.save(driver);
    }

    @Transactional(readOnly = true)
    public Driver getDriverById(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Driver", id));
    }

    @Transactional
    public Driver updateDriverStatusById(Long id, DriverStatus status) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Driver", id));
        driver.setStatus(status);
        return driverRepository.save(driver);
    }

    @Transactional
    public Driver assignAvailableDriver() {
        Driver driver = driverRepository.findFirstFreeDriverForUpdate(DriverStatus.AVAILABLE)
                .orElseThrow(NoAvailableDriversException::new);
        driver.setStatus(DriverStatus.BUSY);
        return driverRepository.save(driver);
    }
}
