package com.trading.pojo;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateCompanyPojo {
        private String name;
        private String symbol;
        private Long totalShares;
        private BigDecimal referencePrice;

        @NotNull(message = "IPO open time is required")
        @Future(message = "IPO open time must be in the future")
        private LocalDateTime ipoOpensAt;

        @NotNull(message = "IPO close time is required")
        @Future(message = "IPO close time must be in the future")
        private LocalDateTime ipoClosesAt;
}
