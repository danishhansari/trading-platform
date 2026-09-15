package com.trading.pojo;

import com.trading.enums.OrderSide;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderPojo {
    private Long companyId;
    private OrderSide side;
    private Long quantity;
    private BigDecimal price;
}