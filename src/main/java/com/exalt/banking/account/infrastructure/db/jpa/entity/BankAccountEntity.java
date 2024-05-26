package com.exalt.banking.account.infrastructure.db.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BANK_ACCOUNT")
public class BankAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @NotNull
    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "overdraft_limit")
    private BigDecimal overdraftLimit;

    @NotNull
    @Column(name = "account_type")
    private String accountType;

    @Column(name = "savings_deposit_limit")
    private BigDecimal savingsDepositLimit;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperationEntity> operations = new ArrayList<>();

    public BankAccountEntity() {
    }

    public BankAccountEntity(BigDecimal balance, BigDecimal overdraftLimit, String accountType,
            BigDecimal savingsDepositLimit) {
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
        this.accountType = accountType;
        this.savingsDepositLimit = savingsDepositLimit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BankAccountEntity that = (BankAccountEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(balance, that.balance)
                && Objects.equals(overdraftLimit, that.overdraftLimit)
                && Objects.equals(accountType, that.accountType)
                && Objects.equals(savingsDepositLimit, that.savingsDepositLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, overdraftLimit, accountType, savingsDepositLimit);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getSavingsDepositLimit() {
        return savingsDepositLimit;
    }

    public void setSavingsDepositLimit(BigDecimal savingsDepositLimit) {
        this.savingsDepositLimit = savingsDepositLimit;
    }

    public List<OperationEntity> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationEntity> operations) {
        this.operations = operations;
    }
}
