package com.exalt.banking.account.domain.service;

import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.Operation;

public interface AccountService {

    void addOperation(long id, Operation operation);

    long createAccount(BankAccount bankAccount);

    BankAccount getAccount(long id);

}
