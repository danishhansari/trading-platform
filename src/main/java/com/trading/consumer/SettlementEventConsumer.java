package com.trading.consumer;

import com.trading.event.TradesMatchedEvent;
import com.trading.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SettlementEventConsumer {

    private final SettlementService settlementService;

    @KafkaListener(topics = "trades", groupId = "settlement-group", concurrency = "4")
    public void onTradesMatched(TradesMatchedEvent event, Acknowledgment ack) {
        settlementService.settle(event.tradeIds());
        ack.acknowledge();
    }
}