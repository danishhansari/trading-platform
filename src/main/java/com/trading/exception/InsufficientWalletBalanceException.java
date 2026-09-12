package com.trading.exception;

public class InsufficientWalletBalanceException extends RuntimeException {
    public InsufficientWalletBalanceException(String message) {
        super(message);
    }
}
