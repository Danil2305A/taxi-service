package com.example.mapper;

import com.example.dto.request.RegisterDriverRequest;
import com.example.dto.response.DriverResponse;
import com.example.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class DriverMapper {
    private static PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder encoder) {
        passwordEncoder = encoder;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "passwordHash", source = "password", qualifiedByName = "encodePassword")
    public abstract Driver toEntity(RegisterDriverRequest requestBody);

    @Named("encodePassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public abstract DriverResponse toResponse(Driver driver);
}
