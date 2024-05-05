package com.exalt.banking.account.infrastructure.db.jpa.repository;

import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.domain.ports.OperationRepository;
import com.exalt.banking.account.infrastructure.db.mapper.OperationMapper;
import com.exalt.banking.account.infrastructure.db.jpa.entity.BankAccountEntity;
import com.exalt.banking.account.infrastructure.db.jpa.entity.OperationEntity;

public class OperationRepositoryAdaptor implements OperationRepository {

    private final JpaOperationRepository jpaOperationRepository;
    private final JpaAccountRepository jpaAccountRepository;

    public OperationRepositoryAdaptor(JpaOperationRepository jpaOperationRepository,
            JpaAccountRepository jpaAccountRepository) {
        this.jpaOperationRepository = jpaOperationRepository;
        this.jpaAccountRepository = jpaAccountRepository;
    }

    @Override
    public void saveOperation(Operation operation, long accountId) {
        BankAccountEntity accountEntity = jpaAccountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for ID: " + accountId));
        OperationEntity operationEntity = OperationMapper.toEntity(operation, accountEntity);
        jpaOperationRepository.save(operationEntity).getId();
    }

}
