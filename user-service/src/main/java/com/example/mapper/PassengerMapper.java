package com.example.mapper;

import com.example.dto.request.RegisterPassengerRequest;
import com.example.dto.response.PassengerResponse;
import com.example.entity.Passenger;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Mapper(componentModel = "spring")
public abstract class PassengerMapper {
    private final PasswordEncoder passwordEncoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "encodePassword")
    public abstract Passenger toEntity(RegisterPassengerRequest requestBody);

    @Named("encodePassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    public abstract PassengerResponse toResponse(Passenger passenger);
}
