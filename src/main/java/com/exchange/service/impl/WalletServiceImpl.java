package com.exchange.service.impl;

import com.exchange.assembler.WalletAssembler;
import com.exchange.constants.UserRole;
import com.exchange.dto.WalletDTO;
import com.exchange.entity.User;
import com.exchange.entity.Wallet;
import com.exchange.exception.*;
import com.exchange.repo.UserRepo;
import com.exchange.repo.WalletRepo;
import com.exchange.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.exchange.constants.Constants.MAX_WALLET_BALANCE;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public Wallet createWallet(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        if (user.getRole() != UserRole.TRADER) throw new InitialOwnerTraderException("Wallet can only be created for a trader");

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepo.save(wallet);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletDTO getBalance(Long userId) {
        Wallet wallet = walletRepo.findByUserId(userId)
                .orElseThrow(() -> new WalletException("Wallet not found for user"));
        return WalletAssembler.getInstance().assembleDetails(wallet);
    }

    @Transactional(readOnly = true)
    public Wallet getWallet(Long userId) {
        return walletRepo.findByUserId(userId)
                .orElseThrow(() -> new WalletException("Wallet not found for user"));
    }

    @Override
    @Transactional
    public WalletDTO deposit(Long userId, BigDecimal amount) {
        validateAmount(amount);

        Wallet wallet = getWallet(userId);
        BigDecimal newBalance = wallet.getBalance().add(amount);

        if (newBalance.compareTo(MAX_WALLET_BALANCE) > 0) throw new WalletException("Wallet balance limit exceeded");

        wallet.credit(amount);
        wallet = walletRepo.save(wallet);

        return WalletAssembler.getInstance().assembleDetails(wallet);
    }

    @Override
    @Transactional
    public WalletDTO withdraw(Long userId, BigDecimal amount) {
        validateAmount(amount);
        Wallet wallet = getWallet(userId);
        if (wallet.getBalance().compareTo(amount) < 0) throw new WalletException("Insufficient wallet balance");
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet = walletRepo.save(wallet);
        return  WalletAssembler.getInstance().assembleDetails(wallet);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException("Amount must be greater than zero");
        }
    }
}