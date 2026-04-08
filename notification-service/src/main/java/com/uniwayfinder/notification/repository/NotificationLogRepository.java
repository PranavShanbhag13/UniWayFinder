package com.uniwayfinder.notification.repository;

import com.uniwayfinder.notification.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    /**
     * CORE IDEMPOTENCY CHECK:
     * Before processing any event from RabbitMQ, the consumer will call this method.
     * If it returns true, we know it's a duplicate event and we should safely ignore it.
     */
    boolean existsByEventId(String eventId);

}