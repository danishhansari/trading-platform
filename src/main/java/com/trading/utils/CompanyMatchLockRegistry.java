package com.trading.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CompanyMatchLockRegistry {

    private static final String LOCK_PREFIX = "lock:match:company:";
    private static final long WAIT_SECONDS = 2;
    private static final long LEASE_SECONDS = 10;

    private final DistributedLockExecutor lockExecutor;

    public <T> T withLock(Long companyId, Supplier<T> action) {
        return lockExecutor.executeWithLock(
                LOCK_PREFIX + companyId, WAIT_SECONDS, LEASE_SECONDS, TimeUnit.SECONDS, action);
    }
}
