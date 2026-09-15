package com.trading.service.impl;

import com.trading.assembler.TradeAssembler;
import com.trading.constants.OrderSide;
import com.trading.dto.TradeDTO;
import com.trading.entity.Order;
import com.trading.entity.Trade;
import com.trading.exception.MatchingLockTimeoutException;
import com.trading.repo.OrderRepo;
import com.trading.repo.TradeRepo;
import com.trading.service.MatchingEngineService;
import com.trading.service.SettlementService;
import com.trading.utils.CompanyMatchLockRegistry;
import com.trading.utils.RedisLockService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.trading.constants.Constants.BOOKABLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingEngineServiceImpl implements MatchingEngineService {

    private final OrderRepo orderRepo;
    private final TradeRepo tradeRepo;
    private final SettlementService settlementService;
    private final CompanyMatchLockRegistry companyMatchLockRegistry;
    private final RedisLockService redisLockService;

    @Override
    @Transactional
    public List<TradeDTO> match(Long companyId) {
        boolean token = redisLockService.tryLock(companyId);
        if(!token) {
            throw new MatchingLockTimeoutException("Could not acquire lock for company " + companyId);
        }
        try {
            return doMatch(companyId);
        } finally {
            redisLockService.unlock(companyId);
        }
    }

    public List<TradeDTO> doMatch(Long companyId) {

        List<Order> buys = new ArrayList<>(orderRepo.findByCompanyIdAndSideAndStatusInOrderByPriceDescCreatedAtAsc(
                companyId, OrderSide.BUY, BOOKABLE));
        List<Order> sells = new ArrayList<>(orderRepo.findByCompanyIdAndSideAndStatusInOrderByPriceAscCreatedAtAsc(
                companyId, OrderSide.SELL, BOOKABLE));

        List<Trade> trades = new ArrayList<>();

        for (Order buy : buys) {

            Iterator<Order> sellIt = sells.iterator();

            while (sellIt.hasNext() && buy.getRemainingQuantity() > 0) {
                Order sell = sellIt.next();
                if (buy.getPrice().compareTo(sell.getPrice()) < 0) {
                    break;
                }
                if (buy.getTrader().getId().equals(sell.getTrader().getId())) {
                    continue;
                }
                long executedQty = Math.min(buy.getRemainingQuantity(), sell.getRemainingQuantity());
                BigDecimal executionPrice = buy.getCreatedAt().isBefore(sell.getCreatedAt())
                        ? buy.getPrice()
                        : sell.getPrice();
                buy.reduceRemainingQuantity(executedQty);
                sell.reduceRemainingQuantity(executedQty);
                trades.add(TradeAssembler
                        .getInstance()
                        .assemble(buy, sell, buy.getCompany(), executedQty, executionPrice));

                if (sell.getRemainingQuantity() == 0) {
                    sellIt.remove();
                }
            }
        }
        orderRepo.saveAll(buys);
        orderRepo.saveAll(sells);
        tradeRepo.saveAll(trades);
        settlementService.settle(trades);

        return trades.stream()
                .map(TradeAssembler.getInstance()::assembleDetails).toList();
    }
}