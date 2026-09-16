package com.trading.producers;

import com.trading.event.TradesMatchedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradeEventProducer {
    private final KafkaTemplate<Long, TradesMatchedEvent> kafkaTemplate;

    public void publish(TradesMatchedEvent event) {
        Message<TradesMatchedEvent> message = MessageBuilder
                    .withPayload(event)
                        .setHeader(KafkaHeaders.TOPIC, "trades")
                        .setHeader(KafkaHeaders.KEY, event.companyId())
                        .build();

        kafkaTemplate.send(message);
    }
}