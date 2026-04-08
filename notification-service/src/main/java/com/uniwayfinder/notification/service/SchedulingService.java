package com.uniwayfinder.notification.service;

public interface SchedulingService {
    /**
     * Mounts the reminder into the Java TaskScheduler.
     */
    void scheduleTask(Long reminderId);
}