package com.example.service;

import com.example.client.NotificationServiceClient;
import com.example.client.UserServiceClient;
import com.example.dto.request.CreateNotificationTaskRequest;
import com.example.dto.request.CreateTripRequest;
import com.example.dto.request.UpdateTripStatusRequest;
import com.example.dto.response.DriverResponse;
import com.example.dto.response.StatisticResponse;
import com.example.dto.response.TripResponse;
import com.example.entity.Trip;
import com.example.enums.DriverStatus;
import com.example.enums.RecipientType;
import com.example.enums.TripStatus;
import com.example.exception.IllegalTripStateException;
import com.example.exception.NoAvailableDriversException;
import com.example.exception.TripNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.mapper.TripMapper;
import com.example.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TripService {
    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    private final UserServiceClient userServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    public TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException(
                        String.format("Trip not found with id: %d", id)
                ));
        return tripMapper.toResponse(trip);
    }

    public List<TripResponse> getTripsByPassengerId(Long passengerId) {
        return tripRepository.findByPassengerId(passengerId).stream()
                .map(tripMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripResponse createTrip(CreateTripRequest requestBody) {
        if (userServiceClient.getPassengerById(requestBody.passengerId()).isEmpty()) {
            throw new UserNotFoundException("Passenger", requestBody.passengerId());
        }

        DriverResponse driver;
        try {
            driver = userServiceClient.assignDriver();
        } catch (Exception e) {
            throw new NoAvailableDriversException();
        }

        Trip trip = tripMapper.toEntity(requestBody);
        trip.setDriverId(driver.id());
        trip.setPrice(requestBody.distance() * requestBody.tariff());
        tripRepository.save(trip);

        notificationServiceClient.sendNotificationTask(
                new CreateNotificationTaskRequest(
                        trip.getId(),
                        RecipientType.DRIVER,
                        trip.getDriverId(),
                        String.format("Trip created, driver assigned. Passenger id: %d. Price: %d", trip.getPassengerId(),
                                trip.getPrice())
                )
        );

        return tripMapper.toResponse(trip);
    }

    @Transactional
    public TripResponse updateTripStatusById(Long id, UpdateTripStatusRequest requestBody) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException(
                        String.format("Trip not found with id: %d", id)
                ));

        TripStatus oldStatus = trip.getStatus();
        TripStatus newStatus = requestBody.status();

        trip.setStatus(newStatus);
        Trip updatedTrip = tripRepository.save(trip);

        if (newStatus == TripStatus.COMPLETED) {
            userServiceClient.updateDriverStatusById(trip.getDriverId(), DriverStatus.AVAILABLE);
        }

        notificationServiceClient.sendNotificationTask(
                new CreateNotificationTaskRequest(
                        updatedTrip.getId(),
                        RecipientType.DRIVER,
                        updatedTrip.getDriverId(),
                        String.format("Trip status changed: %s -> %s",
                                oldStatus.name(), newStatus.name())
                )
        );

        return tripMapper.toResponse(updatedTrip);
    }

    @Transactional
    public TripResponse rateTrip(Long tripId, Integer rating) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new TripNotFoundException(
                        String.format("Trip not found with id: %d", tripId)
                ));

        if (trip.getStatus() != TripStatus.COMPLETED) {
            throw new IllegalTripStateException("Cannot rate trip that is not completed");
        }

        trip.setRating(rating);
        Trip updatedTrip = tripRepository.save(trip);

        notificationServiceClient.sendNotificationTask(
                new CreateNotificationTaskRequest(
                        updatedTrip.getId(),
                        RecipientType.PASSENGER,
                        updatedTrip.getPassengerId(),
                        String.format("Trip rated: %d",
                                rating)
                )
        );

        return tripMapper.toResponse(updatedTrip);
    }

    public StatisticResponse getStatisticForDate(String date) {
        LocalDate localDate;
        if (date == null || date.isBlank()) {
            localDate = LocalDate.now(ZoneOffset.UTC);
        } else {
            localDate = LocalDate.parse(date);
        }

        Instant start = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = localDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<Object[]> statisticResult = tripRepository.getTripCountAndAveragePriceBetween(start, end);
        if (statisticResult.isEmpty()) {
            return new StatisticResponse(localDate.toString(), 0L, 0.0);
        }

        Object[] res = statisticResult.getFirst();
        long tripCount = ((Number) res[0]).longValue();
        double averagePrice = res[1] != null ? ((Number) res[1]).doubleValue() : 0.0;

        return new StatisticResponse(localDate.toString(), tripCount, averagePrice);
    }
}
