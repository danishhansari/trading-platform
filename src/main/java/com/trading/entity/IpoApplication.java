package com.trading.entity;


import com.trading.enums.IpoApplicationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "ipo_applications",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"trader_id", "company_id"})}
)
@RequiredArgsConstructor
public class IpoApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trader_id", nullable = false)
    private User trader;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private Long requestedQuantity;

    @Column(nullable = false)
    private Long allocatedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IpoApplicationStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime appliedAt;

    public IpoApplication(User trader, Company company, Long requestedQuantity) {
        this.trader = trader;
        this.company = company;
        this.requestedQuantity = requestedQuantity;
        this.allocatedQuantity = 0L;
        this.status = IpoApplicationStatus.PENDING;
    }

    public void allocate(Long quantity, IpoApplicationStatus resultStatus) {
        if (quantity < 0 || quantity > requestedQuantity) {
            throw new IllegalArgumentException("Invalid allocation quantity");
        }
        this.allocatedQuantity = quantity;
        this.status = resultStatus;
    }
}