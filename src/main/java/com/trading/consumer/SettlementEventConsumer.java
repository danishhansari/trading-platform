package com.trading.consumer;

import com.trading.entity.Trade;
import com.trading.event.TradesMatchedEvent;
import com.trading.repo.TradeRepo;
import com.trading.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SettlementEventConsumer {
    private final TradeRepo tradeRepo;
    private final SettlementService settlementService;

    @KafkaListener(topics = "trades", groupId = "settlement-group", concurrency = "4")
    public void onTradesMatched(TradesMatchedEvent event, Acknowledgment ack) {
        List<Trade> trades = tradeRepo.findAllById(event.tradeIds());
        settlementService.settle(trades);
        ack.acknowledge();
    }
}