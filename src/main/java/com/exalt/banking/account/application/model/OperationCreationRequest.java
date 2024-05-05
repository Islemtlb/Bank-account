package com.exalt.banking.account.application.model;

import java.math.BigDecimal;

import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.domain.model.OperationType;

import jakarta.validation.constraints.NotNull;

public record OperationCreationRequest(@NotNull OperationType type, @NotNull BigDecimal amount) {

    public Operation toOperation() {
        return new Operation(type, amount);
    }
}