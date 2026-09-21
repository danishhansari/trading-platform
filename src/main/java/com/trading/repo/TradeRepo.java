package com.trading.repo;

import com.trading.dto.UserTradeDTO;
import com.trading.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepo extends JpaRepository<Trade, Long> {
    @Query("""
    SELECT new com.trading.dto.UserTradeDTO(
        t.id,
        c.name,
        t.quantity,
        t.price,

        CASE
            WHEN buyTrader.id = :userId THEN 'BUY'
            ELSE 'SELL'
        END,

        new com.trading.dto.UserDTO(
            counterTrader.id,
            counterTrader.name,
            counterTrader.email,
            counterTrader.role,
            null
        ),

        new com.trading.dto.UserDTO(
            buyTrader.id,
            buyTrader.name,
            buyTrader.email,
            buyTrader.role,
            null
        ),

        t.executedAt
    )
    FROM Trade t
    JOIN t.company c
    JOIN t.buyOrder buyOrder
    JOIN t.sellOrder sellOrder
    JOIN buyOrder.trader buyTrader
    JOIN sellOrder.trader sellTrader

    JOIN User counterTrader
        ON counterTrader.id =
            CASE
                WHEN buyTrader.id = :userId THEN sellTrader.id
                ELSE buyTrader.id
            END

    WHERE buyTrader.id = :userId
       OR sellTrader.id = :userId

    ORDER BY t.executedAt DESC
""")
    List<UserTradeDTO> findTradesByUserId(@Param("userId") Long userId);
}
