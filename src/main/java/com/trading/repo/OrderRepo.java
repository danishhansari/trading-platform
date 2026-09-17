package com.trading.repo;

import com.trading.enums.OrderSide;
import com.trading.enums.OrderStatus;
import com.trading.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
    select coalesce(sum(o.remainingQuantity), 0)
    from Order o
    where o.trader.id = :traderId
      and o.company.id = :companyId
      and o.side = :side
      and o.status in :statuses
    """)
    Long sumRemainingQuantityByTraderAndCompanyAndSideAndStatusIn(
            @Param("traderId") Long traderId,
            @Param("companyId") Long companyId,
            @Param("side") OrderSide side,
            @Param("statuses") List<OrderStatus> statuses);
}