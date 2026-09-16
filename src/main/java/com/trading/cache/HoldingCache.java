package com.trading.cache;

import com.trading.entity.Holding;
import com.trading.repo.HoldingRepo;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HoldingCache {

    private final RedissonClient redissonClient;
    private final HoldingRepo holdingRepo;

    private static final Duration TTL = Duration.ofMinutes(5);

    private String key(Long userId, Long companyId) {
        return "holding:{" + userId + "}:" + companyId;
    }

    public Optional<Long>getQuantity(Long userId, Long companyId) {
        RBucket<Long> bucket = redissonClient.getBucket(key(userId, companyId));
        Long cached = bucket.get();
        if (cached != null) {
            return Optional.of(cached);
        }

        Optional<Holding> holding = holdingRepo.findByUserIdAndCompanyId(userId, companyId);
        holding.ifPresent(h -> bucket.set(h.getQuantity(), TTL));
        return holding.map(Holding::getQuantity);
    }

    public void invalidate(Long userId, Long companyId) {
        redissonClient.getBucket(key(userId, companyId)).delete();
    }
}