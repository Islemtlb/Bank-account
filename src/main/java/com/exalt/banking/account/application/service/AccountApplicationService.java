package com.exalt.banking.account.application.service;

import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.domain.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountApplicationService {

    private final AccountService accountService;

    public AccountApplicationService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Transactional
    public void addOperation(long accountId, Operation operation) {
        accountService.addOperation(accountId, operation);
    }

    @Transactional
    public long createAccount(BankAccount bankAccount) {
        return accountService.createAccount(bankAccount);
    }

    @Transactional
    public BankAccount getAccount(long accountId) {
        return accountService.getAccount(accountId);
    }

}
