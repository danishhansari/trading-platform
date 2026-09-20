package com.trading.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketPriceEvent(
        Long companyId,
        BigDecimal lastTradedPrice,
        LocalDateTime tradedAt
) {}