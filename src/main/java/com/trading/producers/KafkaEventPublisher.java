package com.trading.producers;

import com.trading.event.OrderPlacedEvent;
import com.trading.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final OrderEventProducer orderEventProducer;
    private final UserEventProducer userEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderPersisted(OrderPlacedEvent event) {
        orderEventProducer.publish(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserCreated(UserCreatedEvent event) {userEventProducer.publish(event);}
}