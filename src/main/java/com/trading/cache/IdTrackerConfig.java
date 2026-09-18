package com.trading.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdTrackerConfig {

    @Bean
    public BoundedIdTracker processedOrderTracker() {
        return new BoundedIdTracker(100_000);
    }

    @Bean
    public BoundedIdTracker processedTradeTracker() {
        return new BoundedIdTracker(100_000);
    }
}