package com.uniwayfinder.notification.service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * An internal application event fired when a new reminder needs to be scheduled.
 */
@Getter
@AllArgsConstructor
public class ReminderScheduledEvent {
    private final Long reminderId;
}