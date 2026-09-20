package com.trading.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableRetry
@EnableScheduling
@Configuration
public class AllConfigs {
}
