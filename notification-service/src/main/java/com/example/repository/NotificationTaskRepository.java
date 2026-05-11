package com.example.repository;

import com.example.entity.NotificationTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    List<NotificationTask> findByTripId(Long tripId);

    @Query(value = "SELECT * FROM notification_tasks WHERE status = 'PENDING' ORDER BY created_at ASC LIMIT 1 FOR UPDATE SKIP LOCKED",
            nativeQuery = true)
    Optional<NotificationTask> findPendingTaskForUpdate();
}
