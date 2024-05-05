package com.exalt.banking.account.infrastructure.db.mapper;

import com.exalt.banking.account.infrastructure.db.jpa.entity.OperationEntity;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.infrastructure.db.jpa.entity.BankAccountEntity;

public final class OperationMapper {
    private OperationMapper() {
        // left empty intentionally
    }

    public static Operation toDomain(OperationEntity entity) {
        if (entity == null) {
            return null;
        }
        Operation operation = new Operation(entity.getType(), entity.getAmount());
        operation.setId(entity.getId());
        return operation;
    }

    public static OperationEntity toEntity(Operation operation, BankAccountEntity bankAccountEntity) {
        if (operation == null) {
            return null;
        }
        OperationEntity operationEntity = new OperationEntity(operation.getType(), operation.getAmount(),
                bankAccountEntity);
        operationEntity.setId(operation.getId());
        return operationEntity;
    }
}
