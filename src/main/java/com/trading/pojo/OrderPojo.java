package com.trading.pojo;

import com.trading.enums.OrderSide;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderPojo {
    @NotNull(message = "Company id is required")
    private Long companyId;

    private OrderSide side;

    @NotNull(message = "quantity is required")
    @Positive(message = "Quantity must be positive or more")
    private Long quantity;

    @NotNull(message = "quantity is required")
    @Positive(message = "Quantity must be positive or more")
    private BigDecimal price;
}