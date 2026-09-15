package com.trading.consumer;

import com.trading.event.UserCreatedEvent;
import com.trading.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventConsumer {
    private final WalletService walletService;

    @KafkaListener(topics = "users", groupId = "matching-engine-group")
    public void consume(UserCreatedEvent event) {
        walletService.createWallet(event.userId());
    }
}
