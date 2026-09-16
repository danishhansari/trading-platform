package com.trading.risk.rules;

import com.trading.cache.HoldingCache;
import com.trading.entity.Company;
import com.trading.entity.Holding;
import com.trading.entity.User;
import com.trading.enums.OrderSide;
import com.trading.pojo.OrderPojo;
import com.trading.repo.HoldingRepo;
import com.trading.risk.RiskRule;
import com.trading.exception.RiskViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.trading.constants.Constants.MAX_POSITION_PER_COMPANY;

@Component
@RequiredArgsConstructor
public class PositionLimitRule implements RiskRule {

    private final HoldingCache holdingCache;

    @Override
    public void check(User trader, Company company, OrderPojo pojo) {
        if (pojo.getSide() != OrderSide.BUY) {
            return;
        }

        long currentHolding = holdingCache.getQuantity(trader.getId(), company.getId())
                        .orElse(0L);

        if (currentHolding + pojo.getQuantity() > MAX_POSITION_PER_COMPANY) {
            throw new RiskViolationException(
                    "Order would bring position to " + (currentHolding + pojo.getQuantity())
                            + ", exceeding max position " + MAX_POSITION_PER_COMPANY + " for this company");
        }
    }
}