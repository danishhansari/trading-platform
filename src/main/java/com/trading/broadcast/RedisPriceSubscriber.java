package com.trading.broadcast;


import com.trading.event.MarketPriceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPriceSubscriber implements MessageListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final RawPriceWebSocketHandler rawPriceWebSocketHandler;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            MarketPriceEvent event = objectMapper.readValue(message.getBody(), MarketPriceEvent.class);

            messagingTemplate.convertAndSend("/topic/price/" + event.companyId(), event);
            rawPriceWebSocketHandler.broadcast(event.companyId(), new String(message.getBody()));

        } catch (Exception e) {
            log.error("Failed to process Redis price message", e);
        }
    }

}