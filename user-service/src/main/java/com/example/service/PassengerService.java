package com.example.service;

import com.example.entity.Passenger;
import com.example.exception.DuplicatedUserException;
import com.example.exception.UserNotFoundException;
import com.example.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;

    @Transactional
    public Passenger createPassenger(Passenger passenger) {
        String email = passenger.getEmail();
        if (passengerRepository.existsByEmail(email)) {
            throw new DuplicatedUserException("Passenger", "email", email);
        }

        String phone = passenger.getPhone();
        if (passengerRepository.existsByPhone(phone)) {
            throw new DuplicatedUserException("Passenger", "phone", phone);
        }

        return passengerRepository.save(passenger);
    }

    @Transactional(readOnly = true)
    public Passenger getPassengerById(Long id) {
        return passengerRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Passenger", id));
    }
}
