package com.trading.broadcast;

import com.trading.event.MarketPriceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketDataBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "market-data", groupId = "broadcast-group", concurrency = "4")
    public void broadcast(MarketPriceEvent event) {
        messagingTemplate.convertAndSend("/topic/price/" + event.companyId(), event);
    }
}