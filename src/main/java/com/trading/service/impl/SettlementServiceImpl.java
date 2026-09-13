package com.trading.service.impl;


import com.trading.entity.*;
import com.trading.exception.HoldingNotFoundException;
import com.trading.exception.WalletNotFoundException;
import com.trading.repo.HoldingRepo;
import com.trading.repo.WalletRepo;
import com.trading.service.SettlementService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final WalletRepo walletRepo;
    private final HoldingRepo holdingRepo;

    @Override
    @Retryable(
            retryFor = { OptimisticLockException.class, ObjectOptimisticLockingFailureException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 50, multiplier = 2)
    )
    @Transactional
    public void settle(List<Trade> trades) {
        for (Trade trade : trades) {
            settleOne(trade);
        }
    }

    private void settleOne(Trade trade) {

        User buyer = trade.getBuyOrder().getTrader();
        User seller = trade.getSellOrder().getTrader();
        Company company = trade.getCompany();
        BigDecimal amount = trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity()));

        Wallet buyerWallet = walletRepo.findByUserId(buyer.getId())
                .orElseThrow(() -> new WalletNotFoundException("Buyer wallet not found"));
        Wallet sellerWallet = walletRepo.findByUserId(seller.getId())
                .orElseThrow(() -> new WalletNotFoundException("Seller wallet not found"));

        buyerWallet.debit(amount);
        sellerWallet.credit(amount);

        Holding buyerHolding = holdingRepo.findByUserIdAndCompanyId(buyer.getId(), company.getId())
                .orElseGet(() -> holdingRepo.save(new Holding(buyer, company)));
        Holding sellerHolding = holdingRepo.findByUserIdAndCompanyId(seller.getId(), company.getId())
                .orElseThrow(() -> new HoldingNotFoundException("Seller holding not found"));

        buyerHolding.increase(trade.getQuantity());
        sellerHolding.decrease(trade.getQuantity());

        walletRepo.save(buyerWallet);
        walletRepo.save(sellerWallet);
        holdingRepo.save(buyerHolding);
        holdingRepo.save(sellerHolding);
    }
}