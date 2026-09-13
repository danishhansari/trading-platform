package com.trading.event;

import com.trading.constants.OrderSide;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderPlacedEvent(
        Long orderId,
        Long companyId,
        Long traderId,
        OrderSide side,
        Long quantity,
        BigDecimal price,
        LocalDateTime placedAt
) {}