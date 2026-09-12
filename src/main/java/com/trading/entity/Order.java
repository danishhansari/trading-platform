package com.trading.entity;

import com.trading.constants.OrderSide;
import com.trading.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trader_id", nullable = false)
    private User trader;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Long remainingQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void reduceRemainingQuantity(Long executedQuantity) {
        if (executedQuantity == null || executedQuantity <= 0) {
            throw new IllegalArgumentException("Executed quantity must be greater than zero");
        }
        if (executedQuantity > remainingQuantity) {
            throw new IllegalArgumentException("Executed quantity cannot exceed remaining quantity");
        }
        remainingQuantity -= executedQuantity;
        status = (remainingQuantity == 0) ? OrderStatus.FILLED : OrderStatus.PARTIALLY_FILLED;
    }

    public void cancel() {
        if (this.status != OrderStatus.OPEN && this.status != OrderStatus.PARTIALLY_FILLED) {
            throw new IllegalStateException("Only open or partially filled orders can be cancelled");
        }
        this.status = OrderStatus.CANCELLED;
    }
}