package com.example.service;

import com.example.entity.NotificationTask;
import com.example.enums.NotificationTaskStatus;
import com.example.repository.NotificationTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NotificationTaskService {
    private final NotificationTaskRepository notificationTaskRepository;

    @Transactional
    public NotificationTask createNotificationTask(NotificationTask notificationTask) {
        return notificationTaskRepository.save(notificationTask);
    }

    public List<NotificationTask> getNotificationTaskByTripId(Long tripId) {
        return notificationTaskRepository.findByTripId(tripId);
    }

    @Transactional
    public void processNotificationTask() {
        Optional<NotificationTask> optionalNotificationTask = notificationTaskRepository.findPendingTaskForUpdate();
        if (optionalNotificationTask.isEmpty()) {
            return;
        }

        NotificationTask notificationTask = optionalNotificationTask.get();
        notificationTask.setStatus(NotificationTaskStatus.PROCESSING);
        notificationTaskRepository.save(notificationTask);

        try {
            Thread.sleep(2000);
            notificationTask.setStatus(NotificationTaskStatus.SENT);
        } catch (Exception e) {
            notificationTask.setAttempts(notificationTask.getAttempts() + 1);
            if (notificationTask.getAttempts() >= 3) {
                notificationTask.setStatus(NotificationTaskStatus.FAILED);
            } else {
                notificationTask.setStatus(NotificationTaskStatus.PENDING);
            }
        }

        notificationTaskRepository.save(notificationTask);
    }
}
