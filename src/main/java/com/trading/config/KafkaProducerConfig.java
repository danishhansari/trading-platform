package com.trading.config;

import com.trading.event.OrderPlacedEvent;
import com.trading.event.TradesMatchedEvent;
import com.trading.event.UserCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseProducerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        props.put(ProducerConfig.RETRIES_CONFIG, 2147483647);
        return props;
    }

    @Bean
    public ProducerFactory<Long, OrderPlacedEvent> orderProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseProducerProps());
    }

    @Bean
    public KafkaTemplate<Long, OrderPlacedEvent> orderKafkaTemplate(
            ProducerFactory<Long, OrderPlacedEvent> orderProducerFactory) {
        return new KafkaTemplate<>(orderProducerFactory);
    }

    @Bean
    public ProducerFactory<Long, TradesMatchedEvent> tradeProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseProducerProps());
    }

    @Bean
    public KafkaTemplate<Long, TradesMatchedEvent> tradeKafkaTemplate(
            ProducerFactory<Long, TradesMatchedEvent> tradeProducerFactory) {
        return new KafkaTemplate<>(tradeProducerFactory);
    }

    @Bean
    public ProducerFactory<Long, UserCreatedEvent> userProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseProducerProps());
    }

    @Bean
    public KafkaTemplate<Long, UserCreatedEvent> userKafkaTemplate(
            ProducerFactory<Long, UserCreatedEvent> userProducerFactory) {
        return new KafkaTemplate<>(userProducerFactory);
    }

    @Bean
    public ProducerFactory<Object, Object> dlqProducerFactory() {
        return new DefaultKafkaProducerFactory<>(baseProducerProps());
    }

    @Bean
    public KafkaTemplate<Object, Object> dlqKafkaTemplate(
            ProducerFactory<Object, Object> dlqProducerFactory) {
        return new KafkaTemplate<>(dlqProducerFactory);
    }
}