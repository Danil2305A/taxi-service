package com.example.worker;

import com.example.service.NotificationTaskService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Worker implements Runnable {
    private final NotificationTaskService notificationTaskService;

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                notificationTaskService.processNotificationTask();
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.out.println("Error in worker: " + e.getMessage());
            }
        }
    }
}
