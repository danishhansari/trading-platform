package com.trading.assembler;

import com.trading.dto.TradeDTO;
import com.trading.entity.Company;
import com.trading.entity.Order;
import com.trading.entity.Trade;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TradeAssembler {
    public Trade assemble(Order buy, Order sell, Company company, Long executedQty, BigDecimal executionPrice) {
        Trade trade = new Trade();
        trade.setBuyOrder(buy);
        trade.setSellOrder(sell);
        trade.setCompany(company);
        trade.setQuantity(executedQty);
        trade.setPrice(executionPrice);
        return trade;
    }

    public TradeDTO assembleDetails(Trade trade) {
        return new TradeDTO(
                trade.getId(),
                trade.getBuyOrder().getId(),
                trade.getSellOrder().getId(),
                trade.getCompany().getId(),
                trade.getQuantity(),
                trade.getPrice(),
                trade.getExecutedAt(),
                trade.getTradeId()
        );
    }
}
