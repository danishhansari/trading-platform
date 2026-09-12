package com.trading.service.impl;


import com.trading.assembler.OrderAssembler;
import com.trading.constants.OrderSide;
import com.trading.dto.OrderDTO;
import com.trading.repo.OrderRepo;
import com.trading.service.OrderBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.trading.constants.Constants.BOOKABLE;

@Service
@RequiredArgsConstructor
public class OrderBookServiceImpl implements OrderBookService {

    private final OrderRepo orderRepo;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getBuySide(Long companyId) {
        return orderRepo.findByCompanyIdAndSideAndStatusInOrderByPriceDescCreatedAtAsc(
                        companyId, OrderSide.BUY, BOOKABLE)
                .stream()
                .map(OrderAssembler.getInstance()::assembleDetails)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getSellSide(Long companyId) {
        return orderRepo.findByCompanyIdAndSideAndStatusInOrderByPriceAscCreatedAtAsc(
                        companyId, OrderSide.SELL, BOOKABLE)
                .stream()
                .map(OrderAssembler.getInstance()::assembleDetails)
                .toList();
    }
}