package com.trading.producers;

import com.trading.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<Long, OrderPlacedEvent> kafkaTemplate;

    public void publish(OrderPlacedEvent event) {
        Message<OrderPlacedEvent> message = MessageBuilder
                .withPayload(event)
                        .setHeader(KafkaHeaders.TOPIC, "orders")
                        .setHeader(KafkaHeaders.KEY, event.companyId())
                        .build();

        kafkaTemplate.send(message);
    }
}