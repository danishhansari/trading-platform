package com.trading.repo;

import com.trading.enums.OrderSide;
import com.trading.enums.OrderStatus;
import com.trading.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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