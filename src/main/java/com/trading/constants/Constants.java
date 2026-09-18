package com.trading.constants;

import com.trading.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public class Constants {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final BigDecimal MAX_WALLET_BALANCE = new BigDecimal("100000000.00");
    public static final List<OrderStatus> BOOKABLE = List.of(OrderStatus.OPEN, OrderStatus.PARTIALLY_FILLED);
    public static final long MAX_QUANTITY_PER_ORDER = 10_000L;
    public static final BigDecimal COLLAR_PERCENT = new BigDecimal("0.20");
    public static final long MAX_POSITION_PER_COMPANY = 50_000L;
}