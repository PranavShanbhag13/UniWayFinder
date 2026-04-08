package com.uniwayfinder.notification.service.consumer;

import com.rabbitmq.client.Channel;
import com.uniwayfinder.notification.dto.ReminderEvent;
import com.uniwayfinder.notification.entity.NotificationLog;
import com.uniwayfinder.notification.entity.Reminder;
import com.uniwayfinder.notification.entity.ReminderStatus;
import com.uniwayfinder.notification.repository.NotificationLogRepository;
import com.uniwayfinder.notification.repository.ReminderRepository;
import com.uniwayfinder.notification.service.event.ReminderScheduledEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQEventConsumer {

    private final ReminderRepository reminderRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Message listener that consumes events from the configured queue.
     * @Valid ensures the incoming JSON matches our DTO constraints.
     */
    @RabbitListener(queues = "${app.rabbitmq.queue}")
    @Transactional
    public void consumeReminderEvent(@Valid ReminderEvent event,
                                     Channel channel,
                                     @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {

        log.info("Received ReminderEvent from RabbitMQ. EventId: {}", event.getEventId());

        try {
            // 1. Idempotency Check: Detect duplicate events
            // We check the NotificationLog before processing to prevent duplicate notifications.
            if (notificationLogRepository.existsByEventId(event.getEventId())) {
                log.warn("Duplicate event detected, ignoring. EventId: {}", event.getEventId());
                // Acknowledge the duplicate so RabbitMQ removes it from the queue, preventing it from getting stuck
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 2. Map DTO to Entity and Save
            Reminder reminder = Reminder.builder()
                    .userId(event.getUserId())
                    .classSessionId(event.getClassSessionId())
                    .reminderTime(event.getReminderTime())
                    // Offset could be used here to subtract from reminderTime if needed,
                    // depending on how Academic Service sends it.
                    .status(ReminderStatus.PENDING)
                    .build();
            reminderRepository.save(reminder);

            // 3. Save NotificationLog to register this eventId and maintain idempotency
            NotificationLog auditLog = NotificationLog.builder()
                    .userId(event.getUserId())
                    .eventId(event.getEventId())
                    .message("Successfully persisted scheduling request for class: " + event.getClassSessionId())
                    .deliveryStatus("RECEIVED_AND_SAVED")
                    .retryCount(0)
                    .build();
            notificationLogRepository.save(auditLog);

            // 4. Trigger Internal Observer Pattern
            eventPublisher.publishEvent(new ReminderScheduledEvent(reminder.getId()));

            // 5. Manual Acknowledgment: Confirm successful processing
            channel.basicAck(deliveryTag, false);
            log.info("Successfully processed and acknowledged EventId: {}", event.getEventId());

        } catch (Exception e) {
            // Exception handling inside consumer
            log.error("Failed to process EventId: {}. Reason: {}", event.getEventId(), e.getMessage(), e);

            // basicReject with requeue=false prevents "poison pill" messages from infinitely looping
            // and crashing the service. In an advanced setup, this routes to a Dead-Letter Queue.
            channel.basicReject(deliveryTag, false);
        }
    }
}