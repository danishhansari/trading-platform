package com.trading.exception;

public class MatchingLockTimeoutException extends RuntimeException {
    public MatchingLockTimeoutException(String message) {
        super(message);
    }
}
