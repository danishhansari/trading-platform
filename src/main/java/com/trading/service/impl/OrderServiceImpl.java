package com.trading.service.impl;

import com.trading.assembler.OrderAssembler;
import com.trading.entity.User;
import com.trading.enums.OrderSide;
import com.trading.enums.OrderStatus;
import com.trading.dto.OrderDTO;
import com.trading.entity.Company;
import com.trading.entity.Order;
import com.trading.event.OrderPlacedEvent;
import com.trading.exception.*;
import com.trading.pojo.OrderPojo;
import com.trading.repo.OrderRepo;
import com.trading.repo.UserRepo;
import com.trading.risk.RiskEngine;
import com.trading.service.OrderService;
import com.trading.cache.CompanyCache;
import com.trading.cache.HoldingCache;
import com.trading.cache.WalletBalanceCache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final CompanyCache companyCache;
    private final OrderAssembler orderAssembler;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final WalletBalanceCache walletBalanceCache;
    private final HoldingCache holdingCache;
    private final UserRepo userRepo;
    private final RiskEngine riskEngine;

    @Override
    @Transactional
    public OrderDTO placeOrder(Long traderId, OrderPojo pojo, Authentication authentication) {

        User trader = getTrader(traderId, authentication);
        Company company = companyCache.getCompany(pojo.getCompanyId());

        validateOrder(pojo);
        riskEngine.validate(trader, company, pojo);

        if (pojo.getSide() == OrderSide.BUY) validateBuyOrder(traderId, pojo.getQuantity(), pojo.getPrice());
        if (pojo.getSide() == OrderSide.SELL) validateSellOrder(traderId, company.getId(), pojo.getQuantity());

        Order order = orderAssembler.assemble(pojo, company, trader);
        order = orderRepo.save(order);

        applicationEventPublisher.publishEvent(new OrderPlacedEvent(order.getId(),
                company.getId(), traderId, order.getSide(), order.getQuantity(),
                order.getPrice(), order.getCreatedAt()));

        return orderAssembler.assembleDetails(order);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long traderId, Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderException("Order not found"));

        if (!order.getTrader().getId().equals(traderId)) {
            throw new OrderException("Cannot cancel another trader's order");
        }

        if (order.getStatus() != OrderStatus.OPEN && order.getStatus() != OrderStatus.PARTIALLY_FILLED) {
            throw new OrderException("Only open or partially filled orders can be cancelled");
        }

        order.cancel();
        orderRepo.save(order);

        return orderAssembler.assembleDetails(order);
    }

    private User getTrader(Long traderId, Authentication authentication) {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        if (!role.equals("ROLE_TRADER")) {
            throw new IllegalArgumentException("Only traders can place orders");
        }
        return userRepo.getReferenceById(traderId);
    }

    private void validateBuyOrder(Long traderId, Long quantity, BigDecimal price) {
        BigDecimal balance = walletBalanceCache.getBalance(traderId);

        BigDecimal orderValue = price.multiply(BigDecimal.valueOf(quantity));

        if (balance.compareTo(orderValue) < 0) {
            throw new WalletException("Insufficient wallet balance for order");
        }
    }

    private void validateSellOrder(Long traderId, Long companyId, Long quantity) {
        Long holding = holdingCache.getQuantity(traderId, companyId)
                .orElseThrow(() -> new HoldingException("Holding not found for company"));

        if (holding < quantity) {
            throw new HoldingException("Insufficient shares for order");
        }
    }

    private void validateOrder(OrderPojo pojo) {
        if (pojo.getQuantity() == null || pojo.getQuantity() <= 0) {
            throw new OrderException("Quantity must be greater than zero");
        }

        if (pojo.getPrice() == null || pojo.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OrderException("Price must be greater than zero");
        }

        if (pojo.getSide() == null) {
            throw new OrderException("Order side is required");
        }
    }
}