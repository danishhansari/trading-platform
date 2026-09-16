package com.trading.risk.rules;

import com.trading.entity.Company;
import com.trading.entity.User;
import com.trading.pojo.OrderPojo;
import com.trading.risk.RiskRule;
import com.trading.exception.RiskViolationException;
import org.springframework.stereotype.Component;

import static com.trading.constants.Constants.MAX_QUANTITY_PER_ORDER;

@Component
public class MaxOrderSizeRule implements RiskRule {

    @Override
    public void check(User trader, Company company, OrderPojo pojo) {
        if (pojo.getQuantity() > MAX_QUANTITY_PER_ORDER) {
            throw new RiskViolationException(
                    "Order quantity " + pojo.getQuantity() +
                            " exceeds max allowed " + MAX_QUANTITY_PER_ORDER);
        }
    }
}