package com.example.mapper;

import com.example.dto.CreateNotificationTaskRequest;
import com.example.dto.NotificationTaskResponse;
import com.example.entity.NotificationTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationTaskMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "attempts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    NotificationTask toEntity(CreateNotificationTaskRequest requestBody);

    NotificationTaskResponse toResponse(NotificationTask entity);
}
