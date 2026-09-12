package com.trading.constants;

import java.math.BigDecimal;
import java.util.List;

public class Constants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final BigDecimal MAX_WALLET_BALANCE = new BigDecimal("1000000.00");
    public static final List<OrderStatus> BOOKABLE = List.of(OrderStatus.OPEN, OrderStatus.PARTIALLY_FILLED);
}