package com.exalt.banking.account.domain.service;

import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.domain.ports.AccountRepository;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void addOperation(long accountId, Operation operation) {
        switch (operation.getType()) {
            case DEBIT -> withdraw(accountId, operation);
            case CREDIT -> deposit(accountId, operation);
            default -> throw new IllegalArgumentException("Unsupported operation type: " + operation.getType());
        }
    }

    @Override
    public long createAccount(BankAccount bankAccount) {
        return accountRepository.createAccount(bankAccount);

    }

    @Override
    public BankAccount getAccount(long id) {
        return accountRepository.getAccount(id);

    }

    private void deposit(long accountId, Operation operation) {
        BankAccount account = accountRepository.getAccount(accountId);
        account.deposit(operation.getAmount());
        accountRepository.updateAccount(account, operation);
    }

    private void withdraw(long accountId, Operation operation) {
        BankAccount account = accountRepository.getAccount(accountId);
        account.withdraw(operation.getAmount());
        accountRepository.updateAccount(account, operation);
    }

}
