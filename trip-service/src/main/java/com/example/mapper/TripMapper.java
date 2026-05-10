package com.example.mapper;

import com.example.dto.request.CreateTripRequest;
import com.example.dto.response.TripResponse;
import com.example.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TripMapper {
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "price", qualifiedByName = "calculatePrice")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "driverId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Trip toEntity(CreateTripRequest requestBody);

    @Named("calculatePrice")
    default Integer calculatePrice(Integer distance, Integer tariff) {
        return distance * tariff;
    }

    TripResponse toResponse(Trip trip);
}
