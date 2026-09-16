package com.trading.service;

import com.trading.dto.OrderDTO;
import com.trading.pojo.OrderPojo;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderDTO placeOrder(Long traderId, OrderPojo pojo, Authentication authentication);
    OrderDTO cancelOrder(Long traderId, Long orderId);
}