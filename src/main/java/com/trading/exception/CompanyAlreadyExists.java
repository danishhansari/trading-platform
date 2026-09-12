package com.trading.exception;


public class CompanyAlreadyExists extends RuntimeException{
    public CompanyAlreadyExists(String message){
        super(message);
    }
}
