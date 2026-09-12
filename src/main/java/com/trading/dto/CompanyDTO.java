package com.trading.dto;

import java.math.BigDecimal;

public record CompanyDTO(
        Long id,
        String name,
        String symbol,
        Long totalShares,
        BigDecimal referencePrice,
        String status
) {
}
