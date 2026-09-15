package com.trading.assembler;

import com.trading.dto.WalletDTO;
import com.trading.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletAssembler {
    public WalletDTO assembleDetails(Wallet wallet) {
        return new WalletDTO(
                wallet.getId(),
                wallet.getUser().getId(),
                wallet.getBalance()
        );
    }
}