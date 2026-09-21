package com.trading.service;

import com.trading.entity.Trade;

import java.util.List;

public interface SettlementService {
    void settle(List<Long> trades);
}
