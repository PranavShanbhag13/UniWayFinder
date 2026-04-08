package com.uniwayfinder.notification.dto;

import com.uniwayfinder.notification.entity.Reminder;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String classSessionId;
    private LocalDateTime reminderTime;
    private String status;

    // Static mapper method to easily convert Entity -> DTO
    public static NotificationResponse fromEntity(Reminder reminder) {
        return NotificationResponse.builder()
                .id(reminder.getId())
                .classSessionId(reminder.getClassSessionId())
                .reminderTime(reminder.getReminderTime())
                .status(reminder.getStatus().name())
                .build();
    }
}