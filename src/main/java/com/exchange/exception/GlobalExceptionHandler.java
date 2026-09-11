package com.exchange.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExists ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }
    @ExceptionHandler(CompanyAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleCompanyAlreadyExists(
            CompanyAlreadyExists ex) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }
    @ExceptionHandler(InitialOwnerTraderException.class)
    public ResponseEntity<ErrorResponse> handleInitialOwnerTraderException(
            InitialOwnerTraderException ex) {

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
}