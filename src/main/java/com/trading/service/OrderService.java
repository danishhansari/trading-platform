package com.trading.service;

import com.trading.dto.OrderDTO;
import com.trading.pojo.OrderPojo;

public interface OrderService {
    OrderDTO placeOrder(Long traderId, OrderPojo pojo);
}