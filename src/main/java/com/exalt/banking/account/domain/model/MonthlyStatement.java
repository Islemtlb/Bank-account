package com.exalt.banking.account.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class MonthlyStatement {

    private BankAccountType accountType;
    private BigDecimal currentBalance;
    private List<Operation> recentOperations;

    public MonthlyStatement(BankAccountType accountType, BigDecimal currentBalance, List<Operation> recentOperations) {
        this.accountType = accountType;
        this.currentBalance = currentBalance;
        this.recentOperations = recentOperations;
    }

    public BankAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(BankAccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public List<Operation> getRecentOperations() {
        return recentOperations;
    }

    public void setRecentOperations(List<Operation> recentOperations) {
        this.recentOperations = recentOperations;
    }
}