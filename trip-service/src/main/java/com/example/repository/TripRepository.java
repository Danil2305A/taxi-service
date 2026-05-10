package com.example.repository;

import com.example.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByPassengerId(Long passengerId);

    @Query("SELECT COUNT(t), COALESCE(AVG(t.price), 0) FROM Trip t WHERE t.createdAt >= :start AND t.createdAt < :end")
    List<Object[]> getTripCountAndAveragePriceBetween(@Param("start") Instant start, @Param("end") Instant end);
}
