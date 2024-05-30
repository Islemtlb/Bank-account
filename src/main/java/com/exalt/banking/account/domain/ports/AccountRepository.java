package com.exalt.banking.account.domain.ports;

import com.exalt.banking.account.domain.model.BankAccount;

public interface AccountRepository {

    BankAccount getAccount(long accountId);

    long createAccount(BankAccount bankAccount);

    void updateAccount(BankAccount account);
}
