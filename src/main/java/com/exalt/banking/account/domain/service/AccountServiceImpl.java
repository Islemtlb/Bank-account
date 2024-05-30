package com.exalt.banking.account.domain.service;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.model.AccountStatement;
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

    @Override
    public AccountStatement generateMonthlyStatement(Long accountId) {
        BankAccount account = accountRepository.getAccount(accountId);

        ZonedDateTime zonedNow = ZonedDateTime.now();
        ZonedDateTime zonedMonthAgo = zonedNow.minusMonths(1);
        Instant now = zonedNow.toInstant();
        Instant oneMonthAgo = zonedMonthAgo.toInstant();

        List<Operation> operations = account.getOperations().stream()
                .filter(operation -> !operation.getDate().isBefore(oneMonthAgo) && !operation.getDate().isAfter(now))
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .collect(Collectors.toList());

        return new AccountStatement(
                account.getAccountType(),
                account.getBalance(),
                operations);
    }

    private void deposit(long accountId, Operation operation) {

        BankAccount account = accountRepository.getAccount(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");

        } else {

            account.deposit(operation.getAmount());
            accountRepository.updateAccount(account);
        }

    }

    private void withdraw(long accountId, Operation operation) {
        BankAccount account = accountRepository.getAccount(accountId);
        if (account == null) {
            throw new AccountNotFoundException("Account not found");

        } else {

            account.withdraw(operation.getAmount());
            accountRepository.updateAccount(account);
        }

    }

}
