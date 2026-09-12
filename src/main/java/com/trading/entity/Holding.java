package com.trading.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "holdings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "company_id"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Holding {

    public Holding(User user, Company company) {
        this.user = user;
        this.company = company;
        this.quantity = 0L;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private Long quantity;

    public void increase(Long amount) {
        if (amount == null || amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero");
        this.quantity += amount;
    }

    public void decrease(Long amount) {
        if (amount == null || amount <= 0) throw new IllegalArgumentException("Amount must be greater than zero");

        if (amount > this.quantity) throw new IllegalArgumentException("Cannot decrease below zero holding");
        this.quantity -= amount;
    }
}
