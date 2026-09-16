package com.trading.risk;

import com.trading.entity.Company;
import com.trading.entity.User;
import com.trading.pojo.OrderPojo;

public interface RiskRule {
    void check(User trader, Company company, OrderPojo pojo);
}