package com.trading.cache;

import com.trading.entity.Company;
import com.trading.exception.CompanyException;
import com.trading.repo.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CompanyCache {

    private final RedissonClient redissonClient;
    private final CompanyRepo companyRepo;

    private static final Duration TTL = Duration.ofMinutes(30);

    private String key(Long companyId) {
        return "company:" + companyId;
    }

    public Company getCompany(Long companyId) {
      Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CompanyException("Company not found"));
        return company;
    }

    public void invalidate(Long companyId) {
        redissonClient.getBucket(key(companyId)).delete();
    }
}