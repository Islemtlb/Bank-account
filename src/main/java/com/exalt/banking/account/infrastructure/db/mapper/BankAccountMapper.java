package com.exalt.banking.account.infrastructure.db.mapper;

import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.BankAccountType;
import com.exalt.banking.account.infrastructure.db.jpa.entity.BankAccountEntity;

import java.util.stream.Collectors;

public final class BankAccountMapper {

    private BankAccountMapper() {
        // left empty intentionally
    }

    public static BankAccount toDomain(BankAccountEntity entity) {
        if (entity == null) {
            return null;
        }
        BankAccount bankAccount = new BankAccount(entity.getBalance(), entity.getOverdraftLimit(),
                BankAccountType.valueOf(entity.getAccountType()),
                entity.getSavingsDepositLimit());
        bankAccount.setId(entity.getId());
        bankAccount.setVersion(entity.getVersion());
        bankAccount.setOperations(entity.getOperations().stream()
                .map(OperationMapper::toDomain)
                .collect(Collectors.toList()));
        return bankAccount;
    }

    public static BankAccountEntity toEntity(BankAccount account) {
        if (account == null) {
            return null;
        }
        BankAccountEntity entity = new BankAccountEntity(account.getBalance(), account.getOverdraftLimit(),
                account.getAccountType().name(), account.getSavingsDepositLimit());
        entity.setId(account.getId());
        entity.setVersion(account.getVersion());
        entity.setOperations(account.getOperations().stream()
                .map(operation -> OperationMapper.toEntity(operation, entity))
                .collect(Collectors.toList()));
        return entity;
    }
}
