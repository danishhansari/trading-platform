package com.trading.consumer;

import com.trading.event.OrderPlacedEvent;
import com.trading.service.MatchingEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final MatchingEngineService matchingEngineService;

    @KafkaListener(topics = "orders", groupId = "matching-engine-group", concurrency = "4")
    public void onOrderPlaced(OrderPlacedEvent event) {
        matchingEngineService.match(event.companyId());
    }

}
