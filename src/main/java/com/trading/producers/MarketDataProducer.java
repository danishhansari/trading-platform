package com.trading.producers;

import com.trading.event.MarketPriceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketDataProducer {

    private final KafkaTemplate<Long, MarketPriceEvent> kafkaTemplate;

    public void publish(MarketPriceEvent event) {
        Message<MarketPriceEvent> message = MessageBuilder
                .withPayload(event)
                        .setHeader(KafkaHeaders.TOPIC, "market-data")
                        .setHeader(KafkaHeaders.KEY, event.companyId())
                        .build();

        kafkaTemplate.send(message);
    }
}