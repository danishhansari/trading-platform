package com.trading.service.impl;

import com.trading.assembler.TradeAssembler;
import com.trading.dto.TradeDTO;
import com.trading.entity.Order;
import com.trading.entity.Trade;
import com.trading.enums.OrderSide;
import com.trading.event.TradesMatchedEvent;
import com.trading.repo.OrderRepo;
import com.trading.repo.TradeRepo;
import com.trading.service.MatchExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.trading.constants.Constants.BOOKABLE;

@Service
@RequiredArgsConstructor
public class MatchExecutionServiceImpl implements MatchExecutionService {
    private final OrderRepo orderRepo;
    private final TradeRepo tradeRepo;
    private final TradeAssembler tradeAssembler;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
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
                trades.add(tradeAssembler
                        .assemble(buy, sell, buy.getCompany(), executedQty, executionPrice));

                if (sell.getRemainingQuantity() == 0) {
                    sellIt.remove();
                }
            }
        }
        orderRepo.saveAll(buys);
        orderRepo.saveAll(sells);
        tradeRepo.saveAll(trades);

        if (!trades.isEmpty()) {
            List<Long> tradeIds = trades.stream().map(Trade::getId).toList();
            applicationEventPublisher.publishEvent(new TradesMatchedEvent(companyId, tradeIds));
        }

        return trades.stream()
                .map(tradeAssembler::assembleDetails).toList();
    }

}
