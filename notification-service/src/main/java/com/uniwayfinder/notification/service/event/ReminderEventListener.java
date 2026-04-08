package com.uniwayfinder.notification.service.event;

import com.uniwayfinder.notification.service.SchedulingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderEventListener {

    private final SchedulingService schedulingService;

    /**
     * Listens for ReminderScheduledEvent.
     * @Async ensures this runs on a separate thread, freeing up the RabbitMQ consumer immediately.
     */
    @Async
    @EventListener
    public void handleReminderScheduled(ReminderScheduledEvent event) {
        log.info("Observer triggered: Scheduling task for Reminder ID {}", event.getReminderId());

        // Pass the ID to the actual scheduling logic
        schedulingService.scheduleTask(event.getReminderId());
    }
}