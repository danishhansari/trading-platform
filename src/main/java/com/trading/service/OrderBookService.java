package com.trading.service;

import com.trading.dto.OrderDTO;
import java.util.List;

public interface OrderBookService {
    List<OrderDTO> getBuySide(Long companyId);
    List<OrderDTO> getSellSide(Long companyId);
}
