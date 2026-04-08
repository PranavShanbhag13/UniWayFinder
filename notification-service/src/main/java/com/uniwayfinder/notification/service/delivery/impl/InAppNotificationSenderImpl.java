package com.uniwayfinder.notification.service.delivery.impl;

import com.uniwayfinder.notification.entity.NotificationLog;
import com.uniwayfinder.notification.entity.Reminder;
import com.uniwayfinder.notification.entity.ReminderStatus;
import com.uniwayfinder.notification.repository.NotificationLogRepository;
import com.uniwayfinder.notification.repository.ReminderRepository;
import com.uniwayfinder.notification.service.delivery.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class InAppNotificationSenderImpl implements NotificationSender {

    private final ReminderRepository reminderRepository;
    private final NotificationLogRepository notificationLogRepository;

    @Override
    @Transactional
    public void send(Reminder reminder) {
        log.info("Delivering In-App notification for user {} for class {}",
                reminder.getUserId(), reminder.getClassSessionId());

        // 1. Update Reminder Status
        reminder.setStatus(ReminderStatus.SENT);
        reminderRepository.save(reminder);

        // 2. Log the successful delivery
        // Note: In a real app, this might also push to a WebSocket or a specific "InAppMessage" table for the UI dropdown.
        NotificationLog deliveryLog = NotificationLog.builder()
                .userId(reminder.getUserId())
                // Generating a unique internal event ID for the delivery log
                .eventId("DELIVERY_" + reminder.getId() + "_" + System.currentTimeMillis())
                .message("Reminder for class " + reminder.getClassSessionId() + " sent successfully.")
                .sentAt(LocalDateTime.now())
                .deliveryStatus("SUCCESS")
                .retryCount(0)
                .build();

        notificationLogRepository.save(deliveryLog);
    }
}