package com.exalt.banking.account.application.model;

import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.BankAccountType;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@ValidSavingsAccountCreationRequest
@ValidCurrentAccountCreationRequest
public record BankAccountCreationRequest(
        @NotNull BigDecimal balance,
        BigDecimal overdraftLimit,
        @NotNull BankAccountType accountType,
        BigDecimal savingsDepositLimit) {

    public BankAccount toBankAccount() {
        return new BankAccount(balance, overdraftLimit, accountType, savingsDepositLimit);
    }
}
