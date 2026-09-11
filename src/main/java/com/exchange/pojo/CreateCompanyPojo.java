package com.exchange.pojo;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateCompanyPojo {
        private String name;
        private String symbol;
        private Long totalShares;
        private BigDecimal referencePrice;
        private Long initialOwnerId;
}
