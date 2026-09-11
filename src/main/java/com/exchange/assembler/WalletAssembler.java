package com.exchange.assembler;

import com.exchange.dto.WalletDTO;
import com.exchange.entity.Wallet;

public class WalletAssembler {

    private static WalletAssembler instance;
    private WalletAssembler() {}

    public static WalletAssembler getInstance() {
        if (instance == null) {
            synchronized (WalletAssembler.class) {
                if (instance == null) {
                    instance = new WalletAssembler();
                }
            }
        }
        return instance;
    }

    public WalletDTO assembleDetails(Wallet wallet) {
        return new WalletDTO(
                wallet.getId(),
                wallet.getUser().getId(),
                wallet.getBalance()
        );
    }
}