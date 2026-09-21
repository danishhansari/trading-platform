package com.trading.consumer;

import com.trading.cache.BoundedIdTracker;
import com.trading.enums.OrderStatus;
import com.trading.event.OrderPlacedEvent;
import com.trading.repo.OrderRepo;
import com.trading.service.MatchingEngineService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final MatchingEngineService matchingEngineService;
    private final OrderRepo orderRepo;
    @Autowired
    @Qualifier("processedOrderTracker")
    private final BoundedIdTracker processedOrderTracker;

    @KafkaListener(topics = "orders", groupId = "matching-engine-group")
    public void onOrderPlaced(OrderPlacedEvent event, Acknowledgment ack) {
        MDC.put("userId", "system-matching"); // or event.traderId().toString() if you want it attributed to the trader
        MDC.put("companyId", event.companyId().toString());
        if (processedOrderTracker.alreadyProcessed(event.orderId())) {
            ack.acknowledge();
            processedOrderTracker.remove(event.orderId());
            return;
        }
        matchingEngineService.match(event.companyId());
        ack.acknowledge();
    }

    @KafkaListener(topics = "orders-dlt", groupId = "dlt-monitor-group")
    public void onDeadLetter(ConsumerRecord<Long, OrderPlacedEvent> record, Acknowledgment ack) {
        OrderPlacedEvent failedEvent = record.value();
        orderRepo.findById(failedEvent.orderId()).ifPresent(order -> {
            order.setStatus(OrderStatus.FAILED);
            orderRepo.save(order);
        });
        ack.acknowledge();
    }


}
