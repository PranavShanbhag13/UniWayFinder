package com.uniwayfinder.notification.service.impl;

import com.uniwayfinder.notification.entity.Reminder;
import com.uniwayfinder.notification.entity.ReminderStatus;
import com.uniwayfinder.notification.repository.ReminderRepository;
import com.uniwayfinder.notification.service.SchedulingService;
import com.uniwayfinder.notification.service.delivery.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService {

    private final ReminderRepository reminderRepository;
    private final TaskScheduler taskScheduler;
    private final NotificationSender notificationSender;

    // Retry Strategy Constants
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 60000; // 1 minute delay between retries

    @Override
    @Transactional
    public void scheduleTask(Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new IllegalArgumentException("Reminder not found with ID: " + reminderId));

        // Time comparison with current system time
        LocalDateTime now = LocalDateTime.now();

        if (reminder.getReminderTime().isBefore(now) || reminder.getReminderTime().isEqual(now)) {
            // Handling of expired reminders: Send immediately
            log.info("Reminder {} time has already passed. Sending immediately.", reminderId);
            executeDelivery(reminder, 1);
        } else {
            // Delayed execution logic
            Instant executionTime = reminder.getReminderTime().atZone(ZoneId.systemDefault()).toInstant();

            // Mark as scheduled in the database
            reminder.setStatus(ReminderStatus.SCHEDULED);
            reminderRepository.save(reminder);

            taskScheduler.schedule(() -> executeDelivery(reminder, 1), executionTime);
            log.info("Scheduled Reminder {} for execution at {}", reminderId, reminder.getReminderTime());
        }
    }

    /**
     * The actual runnable task that gets executed when the timer fires.
     * Includes the retry mechanism for failed deliveries[cite: 94].
     */
    private void executeDelivery(Reminder reminder, int currentAttempt) {
        try {
            // Re-fetch to ensure status hasn't changed (e.g., cancelled by user)
            Reminder currentReminder = reminderRepository.findById(reminder.getId()).orElse(null);
            if (currentReminder != null &&
                    (currentReminder.getStatus() == ReminderStatus.PENDING || currentReminder.getStatus() == ReminderStatus.SCHEDULED)) {

                notificationSender.send(currentReminder);

            } else {
                log.warn("Reminder {} was altered before execution. Skipping.", reminder.getId());
            }
        } catch (Exception e) {
            log.error("Failed to execute delivery for Reminder {} on attempt {}: {}",
                    reminder.getId(), currentAttempt, e.getMessage());

            if (currentAttempt < MAX_RETRIES) {
                // Schedule a retry
                Instant retryTime = Instant.now().plusMillis(RETRY_DELAY_MS);
                taskScheduler.schedule(() -> executeDelivery(reminder, currentAttempt + 1), retryTime);
                log.info("Scheduled retry for Reminder {} at {}", reminder.getId(), retryTime);
            } else {
                // Max retries reached, mark as permanently FAILED
                // Re-fetch to avoid detached entity issues outside of a transaction
                Reminder failedReminder = reminderRepository.findById(reminder.getId()).orElse(null);
                if (failedReminder != null) {
                    failedReminder.setStatus(ReminderStatus.FAILED);
                    reminderRepository.save(failedReminder);
                    log.error("Max retries reached for Reminder {}. Marked as FAILED.", reminder.getId());
                }
            }
        }
    }
}