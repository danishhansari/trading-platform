package com.exchange.service;

import com.exchange.dto.WalletDTO;
import com.exchange.entity.Wallet;
import java.math.BigDecimal;

public interface WalletService {
    Wallet createWallet(Long userId);
    WalletDTO getBalance(Long userId);
    WalletDTO deposit(Long userId, BigDecimal amount);
    WalletDTO withdraw(Long userId, BigDecimal amount);
}