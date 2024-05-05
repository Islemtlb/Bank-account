package com.exalt.banking.account.domain.model;

import com.exalt.banking.account.domain.exceptions.InsufficientFundsException;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BankAccount {

    private Long id;
    private BigDecimal balance;
    private BigDecimal overdraftLimit;
    private List<Operation> operations = new ArrayList<>();
    private Long version;

    public BankAccount(Long version, BigDecimal balance, BigDecimal overdraftLimit) {
        this.version = version;
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
    }

    public BankAccount(@NotNull BigDecimal balance, @NotNull BigDecimal overdraftLimit) {
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(getBalance().add(getOverdraftLimit())) > 0) {
            throw new InsufficientFundsException("Fonds insuffisants");
        }
        setBalance(getBalance().subtract(amount));
        operations.add(new Operation(OperationType.DEBIT, amount));
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
        operations.add(new Operation(OperationType.CREDIT, amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(balance, that.balance) &&
                Objects.equals(overdraftLimit, that.overdraftLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, overdraftLimit);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
