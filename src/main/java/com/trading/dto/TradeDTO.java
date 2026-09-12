package com.trading.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TradeDTO(
        Long id,
        Long buyOrderId,
        Long sellOrderId,
        Long companyId,
        Long quantity,
        BigDecimal price,
        LocalDateTime executedAt
) {}