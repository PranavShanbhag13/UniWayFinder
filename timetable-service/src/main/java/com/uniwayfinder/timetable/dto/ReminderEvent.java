package com.uniwayfinder.timetable.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing the event published by the Timetable Service.
 * This MUST match the ReminderEvent in the Notification Service exactly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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