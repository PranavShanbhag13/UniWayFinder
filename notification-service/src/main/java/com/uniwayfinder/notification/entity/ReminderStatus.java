package com.uniwayfinder.notification.entity;

public enum ReminderStatus {
    PENDING,
    SCHEDULED, // Added this state to map with internal scheduling logic
    SENT,
    READ,
    FAILED
}