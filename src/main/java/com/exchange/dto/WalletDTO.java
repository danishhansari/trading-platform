package com.exchange.dto;

import java.math.BigDecimal;

public record WalletDTO (
    Long id,
    Long userId,
    BigDecimal balance
) {}