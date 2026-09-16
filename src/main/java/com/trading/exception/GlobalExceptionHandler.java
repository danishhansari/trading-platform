package com.trading.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(
            UserException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }
    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<ErrorResponse> handleCompanyExists(
            CompanyException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(HoldingException.class)
    public ResponseEntity<ErrorResponse> handleHoldingException(
            HoldingException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(MatchingLockTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleMatchingLockTimeoutException(
            MatchingLockTimeoutException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(WalletException.class)
    public ResponseEntity<ErrorResponse> handleWalletException(
            WalletException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(
            OrderException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(MatchingFailedException.class)
    public ResponseEntity<ErrorResponse> handleMatchingFailedException(
            MatchingFailedException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(TradeException.class)
    public ResponseEntity<ErrorResponse> handleTradeException(
            TradeException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }
}