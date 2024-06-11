package com.exalt.banking.account.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exalt.banking.account.domain.model.AccountStatement;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.BankAccountType;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.domain.model.OperationType;
import com.exalt.banking.account.domain.service.AccountService;

@ExtendWith(MockitoExtension.class)
class AccountApplicationServiceTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountApplicationService accountApplicationService;

    @Test
    void addOperation_shouldCallAddOperationInService_whenCalled() {
        long accountId = 1L;
        Operation operation = new Operation(OperationType.CREDIT, BigDecimal.TEN);

        accountApplicationService.addOperation(accountId, operation);

        verify(accountService).addOperation(accountId, operation);
    }

    @Test
    void createAccount_shouldReturnAccountId_whenBankAccountIsProvided() {
        BankAccount bankAccount = new BankAccount(BigDecimal.valueOf(50), BigDecimal.valueOf(50),
                BankAccountType.CURRENT,
                BigDecimal.valueOf(200));
        when(accountService.createAccount(bankAccount)).thenReturn(1L);

        accountApplicationService.createAccount(bankAccount);

        verify(accountService).createAccount(bankAccount);
    }

    @Test
    void getAccount_shouldReturnBankAccount_whenAccountIdIsProvided() {
        long accountId = 1L;
        BankAccount expectedBankAccount = new BankAccount(BigDecimal.valueOf(50), BigDecimal.valueOf(50),
                BankAccountType.CURRENT,
                BigDecimal.valueOf(200));
        when(accountService.getAccount(accountId)).thenReturn(expectedBankAccount);

        accountApplicationService.getAccount(accountId);

        verify(accountService).getAccount(accountId);
    }

    @Test
    void generateMonthlyStatement_shouldReturnAccountStatement_whenAccountIdIsProvided() {
        long accountId = 1L;
        AccountStatement expectedAccountStatement = new AccountStatement(null, null, null);
        when(accountService.generateMonthlyStatement(accountId)).thenReturn(expectedAccountStatement);

        accountApplicationService.generateMonthlyStatement(accountId);

        verify(accountService).generateMonthlyStatement(accountId);
    }
}
