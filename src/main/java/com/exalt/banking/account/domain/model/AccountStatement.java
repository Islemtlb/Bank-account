package com.exalt.banking.account.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class AccountStatement {

    private final BankAccountType accountType;
    private final BigDecimal balance;
    private final List<Operation> operations;

    public AccountStatement(BankAccountType accountType, BigDecimal balance, List<Operation> operations) {
        this.accountType = accountType;
        this.balance = balance;
        this.operations = operations;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
