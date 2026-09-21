package com.trading.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@EnableKafka
@EnableRetry
@EnableScheduling
@Configuration
@EnableWebSocketMessageBroker
public class AllConfigs {
}
