package com.trading.controller;

import com.trading.dto.UserTradeDTO;
import com.trading.repo.TradeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final TradeRepo tradeRepo;

    @GetMapping("/trades")
    @PreAuthorize("hasRole('EXCHANGE_ADMIN')")
    public List<UserTradeDTO> findTradesByUserId(@Param("userId") Long userId) {
        List<UserTradeDTO> trades = tradeRepo.findTradesByUserId(userId);
        return trades;
    }
}
