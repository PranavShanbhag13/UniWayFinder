package com.uniwayfinder.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Pulling values from application.yml
    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    /**
     * Define the durable queue.
     * The second parameter 'true' makes it durable, meaning it survives RabbitMQ restarts.
     */
    @Bean
    public Queue reminderQueue() {
        return new Queue(queueName, true);
    }

    /**
     * Define the Topic Exchange.
     * Topic exchanges allow for flexible routing based on wildcard matching.
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName);
    }

    /**
     * Bind the Queue to the Exchange using the specific routing key.
     * Any message sent to this exchange with "academic.reminder.schedule" will be routed to our queue.
     */
    @Bean
    public Binding binding(Queue reminderQueue, TopicExchange topicExchange) {
        return BindingBuilder
                .bind(reminderQueue)
                .to(topicExchange)
                .with(routingKey);
    }

    /**
     * Configure JSON Message Converter.
     * This allows Spring to automatically convert the incoming RabbitMQ byte array
     * into our Java DTOs (like ReminderEvent) using Jackson.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
