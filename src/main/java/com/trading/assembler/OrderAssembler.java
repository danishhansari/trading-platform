package com.trading.assembler;

import com.trading.enums.OrderStatus;
import com.trading.dto.OrderDTO;
import com.trading.entity.Company;
import com.trading.entity.Order;
import com.trading.entity.User;
import com.trading.pojo.OrderPojo;

public class OrderAssembler {

    private static OrderAssembler instance;

    private OrderAssembler() {}

    public static OrderAssembler getInstance() {
        if (instance == null) {
            synchronized (OrderAssembler.class) {
                if (instance == null) {
                    instance = new OrderAssembler();
                }
            }
        }
        return instance;
    }

    public Order assemble(OrderPojo request,Company company, User trader) {
        Order order = new Order();
        order.setTrader(trader);
        order.setCompany(company);
        order.setSide(request.getSide());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        order.setRemainingQuantity(request.getQuantity());
        order.setStatus(OrderStatus.OPEN);
        return order;
    }

    public OrderDTO assembleDetails(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getTrader().getId(),
                order.getCompany().getId(),
                order.getSide(),
                order.getQuantity(),
                order.getRemainingQuantity(),
                order.getPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}