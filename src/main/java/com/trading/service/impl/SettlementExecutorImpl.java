package com.trading.service.impl;

import com.trading.entity.*;
import com.trading.enums.TradeStatus;
import com.trading.exception.HoldingException;
import com.trading.exception.TradeException;
import com.trading.repo.HoldingRepo;
import com.trading.repo.TradeRepo;
import com.trading.service.SettlementExecutor;
import com.trading.service.WalletService;
import com.trading.cache.CompanyCache;
import com.trading.cache.HoldingCache;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SettlementExecutorImpl implements SettlementExecutor {

    private final HoldingRepo holdingRepo;
    private final TradeRepo tradeRepo;
    private final CompanyCache companyCache;
    private final WalletService walletService;
    private final HoldingCache holdingCache;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3, backoff = @Backoff(delay = 50))
    public void attemptSettle(Long tradeId) {
        Trade trade = tradeRepo.findById(tradeId)
                .orElseThrow(() -> new TradeException("Trade not found: " + tradeId));

        if (trade.getTradeStatus() == TradeStatus.SETTLED) {
            return;
        }

        User buyer = trade.getBuyOrder().getTrader();
        User seller = trade.getSellOrder().getTrader();
        Company company = companyCache.getCompany(trade.getCompany().getId());
        BigDecimal amount = trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity()));

        walletService.withdraw(buyer.getId(), amount);
        walletService.deposit(seller.getId(), amount);

        Holding buyerHolding = holdingRepo.findByUserIdAndCompanyId(buyer.getId(), company.getId())
                .orElseGet(() -> holdingRepo.save(new Holding(buyer, company)));
        Holding sellerHolding = holdingRepo.findByUserIdAndCompanyId(seller.getId(), company.getId())
                .orElseThrow(() -> new HoldingException("Seller holding not found"));

        buyerHolding.increase(trade.getQuantity());
        sellerHolding.decrease(trade.getQuantity());
        holdingRepo.save(buyerHolding);
        holdingRepo.save(sellerHolding);

        holdingCache.invalidate(buyer.getId(), company.getId());
        holdingCache.invalidate(seller.getId(), company.getId());

        trade.setTradeStatus(TradeStatus.SETTLED);
        tradeRepo.save(trade);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long tradeId, String reason) {
        tradeRepo.findById(tradeId).ifPresent(trade -> {
            trade.setTradeStatus(TradeStatus.SETTLEMENT_FAILED);
            tradeRepo.save(trade);
        });
    }
}