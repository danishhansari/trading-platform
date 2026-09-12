package com.trading.repo;

import com.trading.constants.OrderSide;
import com.trading.constants.OrderStatus;
import com.trading.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByCompanyIdAndSideAndStatusInOrderByPriceDescCreatedAtAsc(
            Long companyId,
            OrderSide side,
            List<OrderStatus> statuses
    );

    List<Order> findByCompanyIdAndSideAndStatusInOrderByPriceAscCreatedAtAsc(
            Long companyId,
            OrderSide side,
            List<OrderStatus> statuses
    );
}