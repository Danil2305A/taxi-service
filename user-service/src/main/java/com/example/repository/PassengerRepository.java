package com.example.repository;

import com.example.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Passenger> findByEmail(String email);
}
