package com.trading.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ordersTopic() { return new NewTopic("orders", 4, (short) 3); }

    @Bean
    public NewTopic ordersDltTopic() { return new NewTopic("orders-dlt", 4, (short) 3); }

    @Bean
    public NewTopic tradesTopic() { return new NewTopic("trades", 4, (short) 3); }

    @Bean
    public NewTopic tradesDltTopic() { return new NewTopic("trades-dlt", 4, (short) 3); }

    @Bean
    public NewTopic usersTopic() {
        return new NewTopic("users", 4, (short) 3);
    }

    @Bean
    public NewTopic usersDltTopic() {
        return new NewTopic("users-dlt", 4, (short) 3);
    }

    @Bean
    public NewTopic marketDataTopic() { return new NewTopic("market-data", 4, (short) 3); }

    @Bean
    public NewTopic marketDataDltTopic() { return new NewTopic("market-data-dlt", 4, (short) 3);}
}