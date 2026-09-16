package com.trading.exception;

public class RiskViolationException extends RuntimeException {
    public RiskViolationException(String message) {
        super(message);
    }
}
