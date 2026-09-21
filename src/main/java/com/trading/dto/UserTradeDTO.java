package com.trading.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserTradeDTO(
        Long tradeId,
        String companyName,
        Long quantity,
        BigDecimal price,
        String tradeType,
        UserDTO buyer,
        UserDTO seller,
        LocalDateTime executedAt
) {}
