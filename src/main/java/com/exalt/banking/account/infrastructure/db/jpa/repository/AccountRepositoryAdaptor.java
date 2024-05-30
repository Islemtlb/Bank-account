package com.exalt.banking.account.infrastructure.db.jpa.repository;

import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.ports.AccountRepository;
import com.exalt.banking.account.infrastructure.db.mapper.BankAccountMapper;

public class AccountRepositoryAdaptor implements AccountRepository {

    private JpaAccountRepository jpaAccountRepository;

    public AccountRepositoryAdaptor(JpaAccountRepository jpaAccountRepository) {
        this.jpaAccountRepository = jpaAccountRepository;
    }

    @Override
    public BankAccount getAccount(long id) {
        return BankAccountMapper.toDomain(jpaAccountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found for ID: " + id)));
    }

    @Override
    public void updateAccount(BankAccount account) {
        jpaAccountRepository.save(BankAccountMapper.toEntity(account));
    }

    @Override
    public long createAccount(BankAccount bankAccount) {
        return jpaAccountRepository.save(BankAccountMapper.toEntity(bankAccount)).getId();
    }

}
