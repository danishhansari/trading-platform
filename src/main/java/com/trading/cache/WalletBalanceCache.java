package com.trading.cache;

import com.trading.entity.Wallet;
import com.trading.repo.WalletRepo;
import com.trading.exception.WalletException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class WalletBalanceCache {

    private final RedissonClient redissonClient;
    private final WalletRepo walletRepo;

    private static final Duration TTL = Duration.ofMinutes(5);

    private String key(Long userId) {
        return "wallet:{" + userId + "}:balance";
    }

    public BigDecimal getBalance(Long userId) {
        RBucket<BigDecimal> bucket = redissonClient.getBucket(key(userId));
        BigDecimal cached = bucket.get();
        if (cached != null) {
            return cached;
        }
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new WalletException("Wallet not found for user"));
        bucket.set(wallet.getBalance(), TTL);
        return wallet.getBalance();
    }

    public void invalidate(Long userId) {
        redissonClient.getBucket(key(userId)).delete();
    }
}