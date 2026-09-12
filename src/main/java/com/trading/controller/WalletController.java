package com.trading.controller;

import com.trading.dto.WalletDTO;
import com.trading.pojo.WalletTransactionPojo;
import com.trading.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<WalletDTO> deposit(@RequestBody WalletTransactionPojo request, HttpServletRequest  httpServletRequest) {
        Long userId = (Long) httpServletRequest.getAttribute("x-user-id");
        WalletDTO wallet = walletService.deposit(userId, request.getAmount());
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<WalletDTO> withdraw(@RequestBody WalletTransactionPojo request, HttpServletRequest  httpServletRequest) {
        Long userId = (Long) httpServletRequest.getAttribute("x-user-id");
        WalletDTO wallet = walletService.withdraw(userId, request.getAmount());
        return ResponseEntity.ok(wallet);
    }

    @GetMapping
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<WalletDTO> getWallet(HttpServletRequest httpServletRequest) {
        Long userId = (Long) httpServletRequest.getAttribute("x-user-id");
        WalletDTO wallet = walletService.getBalance(userId);
        return ResponseEntity.ok(wallet);
    }

}