package com.trading.consumer;

import com.trading.event.UserCreatedEvent;
import com.trading.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventConsumer {
    private final WalletService walletService;

    @KafkaListener(topics = "users", groupId = "matching-engine-group",  concurrency = "2")
    public void consume(UserCreatedEvent event, Acknowledgment ack) {
        walletService.createWallet(event.userId());
        ack.acknowledge();
    }
}
