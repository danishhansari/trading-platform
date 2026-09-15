package com.trading.service.impl;

import com.trading.assembler.TradeAssembler;
import com.trading.dto.TradeDTO;
import com.trading.exception.MatchingFailedException;
import com.trading.repo.OrderRepo;
import com.trading.repo.TradeRepo;
import com.trading.service.MatchExecutionService;
import com.trading.service.MatchingEngineService;
import com.trading.service.SettlementService;
import com.trading.utils.CompanyMatchLockRegistry;
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

    private final OrderRepo orderRepo;
    private final TradeRepo tradeRepo;
    private final SettlementService settlementService;
    private final CompanyMatchLockRegistry companyMatchLockRegistry;
    private final TradeAssembler tradeAssembler;
    private final MatchExecutionService matchExecutionService;

    @Override
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 3,
            backoff = @Backoff(delay = 50))
    public List<TradeDTO> match(Long companyId) {
        return companyMatchLockRegistry.withLock(companyId, () ->
                matchExecutionService.doMatch(companyId));
    }

    @Recover
    public List<TradeDTO> recoverFromMatchFailure(ObjectOptimisticLockingFailureException e, Long companyId) {
        throw new MatchingFailedException("Unable to match company " + companyId + " after retries");
    }

}