package com.exalt.banking.account.domain.exceptions;

public class DepositCeilingExceededException extends RuntimeException {

    public DepositCeilingExceededException(String message) {
        super(message);
    }
}