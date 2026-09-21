package com.trading.pojo;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateCompanyPojo {

        @NotNull(message = "Company name is required")
        private String name;
        @NotNull(message = "Company symbol is required")
        private String symbol;

        @Positive(message = "Total shares must be greater than zero")
        @NotNull(message = "Total shares is required")
        private Long totalShares;

        @Positive(message = "Quantity must be greater than zero")
        @NotNull(message = "Quantity is required")
        private BigDecimal referencePrice;

        @NotNull(message = "IPO open time is required")
        @Future(message = "IPO open time must be in the future")
        private LocalDateTime ipoOpensAt;

        @NotNull(message = "IPO close time is required")
        @Future(message = "IPO close time must be in the future")
        private LocalDateTime ipoClosesAt;
}
