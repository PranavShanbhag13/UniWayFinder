package com.uniwayfinder.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs", indexes = {
        // Indexing event_id to quickly check for duplicate messages from RabbitMQ
        @Index(name = "idx_event_id", columnList = "event_id", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivery_status", nullable = false)
    private String deliveryStatus;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    // store the unique eventId from RabbitMQ. Before processing a new message,
    // query this column to ensure we haven't seen this eventId before.
    @Column(name = "event_id", unique = true, nullable = false)
    private String eventId;
}