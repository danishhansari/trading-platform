package com.trading.service.impl;

import com.trading.entity.*;
import com.trading.enums.TradeStatus;
import com.trading.exception.HoldingException;
import com.trading.exception.TradeException;
import com.trading.exception.WalletException;
import com.trading.repo.HoldingRepo;
import com.trading.repo.TradeRepo;
import com.trading.repo.WalletRepo;
import com.trading.service.SettlementExecutor;
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

    private final WalletRepo walletRepo;
    private final HoldingRepo holdingRepo;
    private final TradeRepo tradeRepo;

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
        Company company = trade.getCompany();
        BigDecimal amount = trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity()));

        Wallet buyerWallet = walletRepo.findByUserId(buyer.getId())
                .orElseThrow(() -> new WalletException("Buyer wallet not found"));
        Wallet sellerWallet = walletRepo.findByUserId(seller.getId())
                .orElseThrow(() -> new WalletException("Seller wallet not found"));

        buyerWallet.debit(amount);
        sellerWallet.credit(amount);

        Holding buyerHolding = holdingRepo.findByUserIdAndCompanyId(buyer.getId(), company.getId())
                .orElseGet(() -> holdingRepo.save(new Holding(buyer, company)));
        Holding sellerHolding = holdingRepo.findByUserIdAndCompanyId(seller.getId(), company.getId())
                .orElseThrow(() -> new HoldingException("Seller holding not found"));

        buyerHolding.increase(trade.getQuantity());
        sellerHolding.decrease(trade.getQuantity());

        walletRepo.save(buyerWallet);
        walletRepo.save(sellerWallet);
        holdingRepo.save(buyerHolding);
        holdingRepo.save(sellerHolding);

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