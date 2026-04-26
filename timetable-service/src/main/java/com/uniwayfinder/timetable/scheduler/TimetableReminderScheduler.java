package com.uniwayfinder.timetable.scheduler;

import com.uniwayfinder.timetable.dto.ReminderEvent;
import com.uniwayfinder.timetable.entity.Timetable;
import com.uniwayfinder.timetable.repository.TimetableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class TimetableReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(TimetableReminderScheduler.class);

    private final TimetableRepository timetableRepository;
    private final RabbitTemplate rabbitTemplate;

    // Fetch routing details from Nacos configuration
    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public TimetableReminderScheduler(TimetableRepository timetableRepository, RabbitTemplate rabbitTemplate) {
        this.timetableRepository = timetableRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Cron Job: Executes at the 0th second of every minute.
     * Scans the database for classes starting in exactly 30 minutes.
     */
    @Scheduled(cron = "0 * * * * ?")
    public void scanAndPublishClassReminders() {
        LocalTime now = LocalTime.now();
        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();

        // limite the time window in the minute just after 30 minutes
        LocalTime targetStart = now.plusMinutes(30).withSecond(0).withNano(0);
        LocalTime targetEnd = now.plusMinutes(30).withSecond(59).withNano(999999999);

        log.debug("scan the coming course after 30 minutes... target: {} 到 {}", targetStart, targetEnd);

        // 1. Query the database for matching classes
        List<Timetable> upcomingClasses = timetableRepository
                .findByDayOfWeekAndStartTimeBetween(currentDayOfWeek, targetStart, targetEnd);

        LocalDate today = LocalDate.now();

        // 2. Iterate through results and publish events to RabbitMQ
        for (Timetable t : upcomingClasses) {
            LocalDateTime classStartTime = LocalDateTime.of(today, t.getStartTime());

            // Build the exact ReminderEvent expected by the Notification Service
            ReminderEvent event = new ReminderEvent();
            event.setUserId(t.getUserId());
            event.setClassSessionId(t.getCourseId());
            event.setReminderTime(classStartTime);
            event.setOffset(30);

            // Idempotency Key
            String uniqueEventId = "TIMETABLE-" + t.getId() + "-" + today.toString();
            event.setEventId(uniqueEventId);

            // 3. Fire and Forget: Send to RabbitMQ
            rabbitTemplate.convertAndSend(exchange, routingKey, event);

            log.info("Publish RabbitMQ -> EventID: {}, UserID: {}, ClassSessionID: {}",
                    event.getEventId(), event.getUserId(), event.getClassSessionId());
        }
    }

}