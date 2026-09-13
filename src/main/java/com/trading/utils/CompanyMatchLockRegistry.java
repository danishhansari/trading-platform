package com.trading.utils;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class CompanyMatchLockRegistry {

    private final ConcurrentHashMap<Long, Object> locks = new ConcurrentHashMap<>();

    public Object lockFor(Long companyId) {
        return locks.computeIfAbsent(companyId, id -> new Object());
    }
}
