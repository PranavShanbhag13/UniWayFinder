package com.uniwayfinder.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing the event published by the Academic Service.
 */
@Data
public class ReminderEvent {

    @NotBlank(message = "User ID cannot be blank")
    private String userId;

    @NotBlank(message = "Class Session ID cannot be blank")
    private String classSessionId;

    @NotNull(message = "Reminder time cannot be null")
    private LocalDateTime reminderTime;

    private Integer offset;

    @NotBlank(message = "Event ID cannot be blank for idempotency")
    private String eventId;
}