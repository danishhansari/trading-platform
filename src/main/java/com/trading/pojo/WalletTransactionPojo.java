package com.trading.pojo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WalletTransactionPojo {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount is required greater than zero")
    private BigDecimal amount;
}
