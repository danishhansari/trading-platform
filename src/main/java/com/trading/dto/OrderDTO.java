package com.trading.dto;


import com.trading.enums.OrderSide;
import com.trading.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(
        Long id,
        Long traderId,
        Long companyId,
        OrderSide side,
        Long quantity,
        Long remainingQuantity,
        BigDecimal price,
        OrderStatus status,
        LocalDateTime createdAt
) {
}