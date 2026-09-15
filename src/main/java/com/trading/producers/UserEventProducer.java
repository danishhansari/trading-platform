package com.trading.producers;


import com.trading.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private final KafkaTemplate<Long, UserCreatedEvent> kafkaTemplate;

    public void publish(UserCreatedEvent event) {
        Message<UserCreatedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "users")
                .setHeader(KafkaHeaders.KEY, event.userId())
                .build();

        kafkaTemplate.send(message);
    }
}