package com.trading.service;

public interface SettlementExecutor {
    void attemptSettle(Long tradeId);
    void markFailed(Long tradeId, String reason);
}
