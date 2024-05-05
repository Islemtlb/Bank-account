package com.exalt.banking.account.application.model;

import java.math.BigDecimal;

import com.exalt.banking.account.domain.model.BankAccount;

import jakarta.validation.constraints.NotNull;

@ValidBalance
public record BankAccountCreationRequest(@NotNull BigDecimal balance, @NotNull BigDecimal overdraftLimit) {

    public BankAccount toBankAccount() {
        return new BankAccount(balance, overdraftLimit);
    }
}