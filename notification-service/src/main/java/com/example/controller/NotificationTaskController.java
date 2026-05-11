package com.example.controller;

import com.example.dto.CreateNotificationTaskRequest;
import com.example.dto.NotificationTaskResponse;
import com.example.entity.NotificationTask;
import com.example.mapper.NotificationTaskMapper;
import com.example.service.NotificationTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationTaskController {
    private final NotificationTaskService notificationTaskService;
    private final NotificationTaskMapper notificationTaskMapper;

    @PostMapping
    public NotificationTaskResponse createNotificationTask(@Valid @RequestBody
                                                           CreateNotificationTaskRequest requestBody) {
        NotificationTask notificationTask = notificationTaskMapper.toEntity(requestBody);
        NotificationTask createdNotificationTask = notificationTaskService.createNotificationTask(notificationTask);
        return notificationTaskMapper.toResponse(createdNotificationTask);
    }

    @GetMapping
    public List<NotificationTaskResponse> getNotificationTasksByTripId(@RequestParam("trip_id") Long tripId) {
        List<NotificationTask> notificationTaskList = notificationTaskService.getNotificationTaskByTripId(tripId);
        return notificationTaskList.stream()
                .map(notificationTaskMapper::toResponse)
                .collect(Collectors.toList());
    }
}
