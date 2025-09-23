package com.bank.Bank.exception;

public class WrongInputException extends RuntimeException {
    public WrongInputException(String msg) {
        super(msg);
    }
}
