package com.trading.risk.rules;

import com.trading.entity.Company;
import com.trading.entity.User;
import com.trading.pojo.OrderPojo;
import com.trading.risk.RiskRule;
import com.trading.exception.RiskViolationException;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

import static com.trading.constants.Constants.COLLAR_PERCENT;

@Component
public class PriceCollarRule implements RiskRule {

    @Override
    public void check(User trader, Company company, OrderPojo pojo) {
        BigDecimal referencePrice = company.getReferencePrice();
        if (referencePrice == null || referencePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        BigDecimal upperBound = referencePrice.multiply(BigDecimal.ONE.add(COLLAR_PERCENT));
        BigDecimal lowerBound = referencePrice.multiply(BigDecimal.ONE.subtract(COLLAR_PERCENT));

        if (pojo.getPrice().compareTo(upperBound) > 0 || pojo.getPrice().compareTo(lowerBound) < 0) {
            throw new RiskViolationException(
                            "Order price " + pojo.getPrice() +
                            " is outside the allowed collar ["
                            + lowerBound + ", " +
                              upperBound + "] around reference price " + referencePrice);
        }
    }
}