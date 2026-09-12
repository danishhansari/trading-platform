package com.trading.assembler;

import com.trading.dto.WalletDTO;
import com.trading.entity.Wallet;

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