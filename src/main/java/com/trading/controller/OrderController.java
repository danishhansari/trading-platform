package com.trading.controller;

import com.trading.dto.OrderDTO;
import com.trading.pojo.OrderPojo;
import com.trading.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderPojo pojo,
                                               HttpServletRequest httpServletRequest,
                                               Authentication authentication) {
        Long traderId = (Long) httpServletRequest.getAttribute("x-user-id");
        OrderDTO orderDTO = orderService.placeOrder(traderId, pojo, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
        Long traderId = (Long) httpServletRequest.getAttribute("x-user-id");
        OrderDTO orderDTO = orderService.cancelOrder(traderId, orderId);
        return ResponseEntity.ok(orderDTO);
    }
}
