package com.trading.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class TraderPositionLockService {
    private static final String LOCK_PREFIX = "lock:position:";
    private static final long WAIT_SECONDS = 2;
    private static final long LEASE_SECONDS = 5;

    private final DistributedLockExecutor lockExecutor;

    public <T> T withLock(Long traderId, Long companyId, Supplier<T> action) {
        return lockExecutor.executeWithLock(
                LOCK_PREFIX + traderId + ":" + companyId, WAIT_SECONDS, LEASE_SECONDS, TimeUnit.SECONDS, action);
    }
}