package com.trading.consumer;

import com.trading.event.MarketPriceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketDataBroadcastConsumer {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "market-data", groupId = "broadcast-group", concurrency = "4")
    public void relayToRedis(MarketPriceEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend("price-updates:" + event.companyId(), json);
        } catch (Exception e) {
            log.error("Failed to serialize/publish market data event for company {}", event.companyId(), e);
        }
    }
}