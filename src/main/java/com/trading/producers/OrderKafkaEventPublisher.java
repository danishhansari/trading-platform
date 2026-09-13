package com.trading.producers;

import com.trading.event.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderKafkaEventPublisher {

    private final OrderEventProducer orderEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderPersisted(OrderPlacedEvent event) {
        orderEventProducer.publish(event);
    }
}