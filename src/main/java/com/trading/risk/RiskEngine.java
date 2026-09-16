package com.trading.risk;

import com.trading.entity.Company;
import com.trading.entity.User;
import com.trading.pojo.OrderPojo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RiskEngine {

    private final List<RiskRule> rules;

    public void validate(User trader, Company company, OrderPojo pojo) {
        for (RiskRule rule : rules) {
            rule.check(trader, company, pojo);
        }
    }
}