package com.uniwayfinder.notification.controller;

import com.uniwayfinder.notification.dto.ReminderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/notify/test")
@RequiredArgsConstructor
public class TestRabbitController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    /**
     * Receive HTTP request from Postman，and then transfer it to RabbitMQ
     */
    @PostMapping("/send-event")
    public ResponseEntity<String> sendTestEvent(@RequestBody ReminderEvent event) {
        log.info("【Test】Read for sending to MQ: {} with mock message: {}", queueName, event.getEventId());

        // send message to RabbitMQ
        rabbitTemplate.convertAndSend(queueName, event);

        return ResponseEntity.ok("Mock message is already put into RabbitMQ successfully！");
    }
}