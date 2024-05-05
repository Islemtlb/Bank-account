package com.exalt.banking.account.domain.ports;

import com.exalt.banking.account.domain.model.Operation;

public interface OperationRepository {
    void saveOperation(Operation operation, long accountId);
}
