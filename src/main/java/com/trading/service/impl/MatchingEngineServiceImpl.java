package com.trading.service.impl;

import com.trading.dto.TradeDTO;
import com.trading.exception.MatchingFailedException;
import com.trading.service.MatchExecutionService;
import com.trading.service.MatchingEngineService;
import com.trading.utils.CompanyMatchLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingEngineServiceImpl implements MatchingEngineService {

    private final CompanyMatchLockService companyMatchLockService;
    private final MatchExecutionService matchExecutionService;

    @Override
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3,
            backoff = @Backoff(delay = 50))
    public List<TradeDTO> match(Long companyId) {
        return companyMatchLockService.withLock(companyId, () ->
                matchExecutionService.doMatch(companyId));
    }

    @Recover
    public List<TradeDTO> recoverFromMatchFailure(ObjectOptimisticLockingFailureException e, Long companyId) {
        throw new MatchingFailedException("Unable to match company " + companyId + " after retries");
    }

}