package com.exalt.banking.account.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.exceptions.InsufficientFundsException;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.domain.model.OperationType;
import com.exalt.banking.account.domain.ports.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountPort;

    @Mock
    private BankAccount account;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void addOperation_should_make_a_deposit_when_type_is_credit() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation depositOperation = new Operation(OperationType.CREDIT, amount);
        when(accountPort.getAccount(accountId)).thenReturn(account);

        // When
        accountService.addOperation(accountId, depositOperation);

        // Then
        verify(accountPort).getAccount(accountId);
        verify(account).deposit(amount);
        verify(accountPort).updateAccount(account, depositOperation);
    }

    @Test
    void addOperation_should_return_not_found_when_deposit_account_is_not_found() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation operation = new Operation(OperationType.CREDIT, amount);
        when(accountPort.getAccount(accountId)).thenThrow(new AccountNotFoundException("Account not found"));

        // When / Then
        assertThrows(AccountNotFoundException.class, () -> accountService.addOperation(accountId, operation));
        verify(accountPort).getAccount(accountId);
    }

    @Test
    void addOperation_should_make_a_deposit_when_account_exists() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation depositOperation = new Operation(OperationType.CREDIT, amount);
        when(accountPort.getAccount(accountId)).thenReturn(account);

        // When
        accountService.addOperation(accountId, depositOperation);

        // Then
        verify(accountPort).getAccount(accountId);
        verify(account).deposit(amount);
        verify(accountPort).updateAccount(account, depositOperation);
    }

    @Test
    void addOperation_should_throw_exception_when_withdrawing_and_funds_are_insufficient() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(150);
        Operation withDrawOperation = new Operation(OperationType.DEBIT, amount);
        when(accountPort.getAccount(accountId)).thenReturn(account);
        doThrow(new InsufficientFundsException("Fonds insuffisants")).when(account).withdraw(amount);

        // When / Then
        assertThrows(InsufficientFundsException.class, () -> accountService.addOperation(accountId, withDrawOperation));

        verify(accountPort).getAccount(accountId);
        verify(account).withdraw(amount);
    }

    @Test
    void addOperation_should_return_account_not_found_when_account_is_not_found_during_withdraw_operation() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(50);
        Operation withDrawOperation = new Operation(OperationType.DEBIT, amount);
        when(accountPort.getAccount(accountId)).thenThrow(new AccountNotFoundException("Account not found"));

        // When / Then
        assertThrows(AccountNotFoundException.class, () -> accountService.addOperation(accountId, withDrawOperation));
        verify(accountPort).getAccount(accountId);
    }

    @Test
    void createAccount_should_create_account_and_return_id_when_creation_is_successful() {
        // Given
        Long expectedId = 1L;
        BankAccount accountToCreate = new BankAccount(1L, BigDecimal.valueOf(1000), BigDecimal.valueOf(1000));
        when(accountPort.createAccount(accountToCreate)).thenReturn(expectedId);

        // When
        long createdId = accountService.createAccount(accountToCreate);

        // Then
        assertEquals(expectedId, createdId);
        verify(accountPort).createAccount(accountToCreate);
    }

}