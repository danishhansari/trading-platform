package com.trading.producers;

import com.trading.event.MarketPriceEvent;
import com.trading.event.OrderPlacedEvent;
import com.trading.event.TradesMatchedEvent;
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
    private final TradeEventProducer tradeEventProducer;
    private final MarketDataProducer marketDataProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderPersisted(OrderPlacedEvent event) {
        orderEventProducer.publish(event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserCreated(UserCreatedEvent event) { userEventProducer.publish(event);}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTradesMatched(TradesMatchedEvent event) { tradeEventProducer.publish(event);}

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onMarketDataProduce(MarketPriceEvent event) { marketDataProducer.publish(event); }
}