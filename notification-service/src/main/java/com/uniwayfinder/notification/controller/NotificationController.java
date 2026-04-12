package com.uniwayfinder.notification.controller;

import com.uniwayfinder.notification.dto.NotificationResponse;
import com.uniwayfinder.notification.entity.Reminder;
import com.uniwayfinder.notification.entity.ReminderStatus;
import com.uniwayfinder.notification.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {

    private final ReminderRepository reminderRepository;

    /**
     * GET /api/notify/user
     */
    @GetMapping("/user")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(Authentication authentication) {
        // Safely get the username directly from the parsed Token context!
        String username = authentication.getName();
        log.info("Fetching all notifications for authenticated user: {}", username);

        List<Reminder> reminders = reminderRepository.findByUserId(username);

        List<NotificationResponse> responses = reminders.stream()
                .map(NotificationResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * GET /api/notify/unread
     */
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(Authentication authentication) {
        String username = authentication.getName();
        log.info("Fetching unread notifications for authenticated user: {}", username);

        List<Reminder> unreadReminders = reminderRepository.findByUserIdAndStatus(username, ReminderStatus.SENT);

        List<NotificationResponse> responses = unreadReminders.stream()
                .map(NotificationResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * PATCH /api/notify/mark-read/{id}
     */
    @PatchMapping("/mark-read/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("Marking notification {} as read", id);
        return reminderRepository.findById(id).map(reminder -> {
            reminder.setStatus(ReminderStatus.READ);
            reminderRepository.save(reminder);
            return ResponseEntity.noContent().<Void>build(); // 204 No Content
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/notify/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        log.info("Deleting notification {}", id);
        if (reminderRepository.existsById(id)) {
            reminderRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}