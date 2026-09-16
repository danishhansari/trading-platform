package com.trading.service;

import com.trading.dto.WalletDTO;
import com.trading.entity.Wallet;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

public interface WalletService {
    Wallet createWallet(Long userId);
    WalletDTO getBalance(Long userId);
    WalletDTO deposit(Long userId, BigDecimal amount);
    WalletDTO withdraw(Long userId, BigDecimal amount);
}