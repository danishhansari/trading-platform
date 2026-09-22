package com.trading.entity;

import com.trading.enums.TradeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Getter
@Setter
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "buy_order_id", nullable = false)
    private Order buyOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sell_order_id", nullable = false)
    private Order sellOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus tradeStatus = TradeStatus.PENDING;

    @Column(name = "settlement_failure_reason")
    private String settlementFailureReason;

    @Column(name = "trade_id", nullable = false, unique = true)
    private String tradeId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime executedAt;

    @PrePersist
    public void generateTradeId() {
        this.tradeId = "#" + this.buyOrder.getId() + "-" + this.sellOrder.getId();
    }
}