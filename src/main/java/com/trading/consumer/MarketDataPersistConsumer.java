package com.trading.consumer;

import com.trading.cache.CompanyCache;
import com.trading.event.MarketPriceEvent;
import com.trading.entity.PriceHistory;
import com.trading.repo.CompanyRepo;
import com.trading.repo.PriceHistoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class MarketDataPersistConsumer {

    private final CompanyRepo companyRepo;
    private final PriceHistoryRepo priceHistoryRepo;
    private final CompanyCache companyCache;

    @KafkaListener(topics = "market-data", groupId = "persister-group", concurrency = "4")
    public void persist(MarketPriceEvent event, Acknowledgment ack) {
        try {
            companyRepo.findById(event.companyId()).ifPresent(company -> {
                company.setLastTradedPrice(event.lastTradedPrice());
                companyRepo.save(company);
                companyCache.invalidate(event.companyId());
            });
            priceHistoryRepo.save(new PriceHistory(event.companyId(), event.lastTradedPrice(), event.tradedAt()));
        } catch (Exception e) {
            throw e;
        } finally {
            ack.acknowledge();
        }
       }
}
