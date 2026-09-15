package com.trading.utils;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisLockService {

    private static final String LOCK_PREFIX = "company-match-lock:";

    private RedissonClient redissonClient;

    public boolean tryLock(Long companyId) {
//        redissonClient = RedisConfig.getRedissonClient();
        String key = LOCK_PREFIX + companyId;
        RLock lock = redissonClient.getLock(key);
//        return lock.tryLock();
        return true;
    }

    public void unlock(Long companyId) {
//        redissonClient = RedisConfig.getRedissonClient();
//        String key = LOCK_PREFIX + companyId;
//
//        RLock lock = redissonClient.getLock(key);
//
//        if (lock.isHeldByCurrentThread()) {
//            lock.unlock();
//        }
    }
}