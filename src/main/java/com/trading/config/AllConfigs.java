package com.trading.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;

@EnableKafka
@EnableRetry
@Configuration
public class AllConfigs {
}
