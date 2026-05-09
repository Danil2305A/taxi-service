package com.example.worker;

import com.example.service.NotificationTaskService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class WorkerPool {
    private final NotificationTaskService notificationTaskService;
    private final ExecutorService executor;
    private final int poolSize;

    public WorkerPool(NotificationTaskService notificationTaskService) {
        this.notificationTaskService = notificationTaskService;
        this.poolSize = 4;
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    @PostConstruct
    public void startWorkers() {
        for (int i = 0; i < poolSize; i++) {
            executor.submit(new Worker(notificationTaskService));
        }
    }

    @PreDestroy
    public void shutdownWorkers() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(20, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
