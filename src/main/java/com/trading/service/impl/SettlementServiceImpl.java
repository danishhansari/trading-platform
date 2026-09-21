package com.trading.service.impl;

import com.trading.entity.*;
import com.trading.exception.HoldingException;
import com.trading.exception.WalletException;
import com.trading.service.SettlementExecutor;
import com.trading.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final SettlementExecutor settlementExecutor;

    @Override
    public void settle(List<Long> trades) {
        for (Long trade : trades) {
            try {
                settlementExecutor.attemptSettle(trade);
            } catch (WalletException | HoldingException e) {
                settlementExecutor.markFailed(trade, e.getMessage());
            }
        }
    }
}