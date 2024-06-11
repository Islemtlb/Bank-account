package com.exalt.banking.account.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;

import com.exalt.banking.account.domain.model.AccountStatement;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.BankAccountType;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.domain.model.OperationType;
import com.exalt.banking.account.domain.ports.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void addOperation_should_make_a_deposit_when_account_exists() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation depositOperation = new Operation(OperationType.CREDIT, amount);

        BankAccount account = new BankAccount(BigDecimal.valueOf(1000), BigDecimal.valueOf(200),
                BankAccountType.CURRENT, BigDecimal.valueOf(100));
        account.setId(accountId);

        when(accountRepository.getAccount(accountId)).thenReturn(account);

        // When
        accountService.addOperation(accountId, depositOperation);

        // Then
        verify(accountRepository).getAccount(accountId);
        assertEquals(BigDecimal.valueOf(1100), account.getBalance());
        verify(accountRepository).updateAccount(account);
    }

    @Test
    void addOperation_should_make_a_withdrawal_when_account_exists() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation withdrawalOperation = new Operation(OperationType.DEBIT, amount);

        BankAccount account = new BankAccount(BigDecimal.valueOf(1000), BigDecimal.valueOf(200),
                BankAccountType.CURRENT, BigDecimal.valueOf(100));
        account.setId(accountId);

        when(accountRepository.getAccount(accountId)).thenReturn(account);

        // When
        accountService.addOperation(accountId, withdrawalOperation);

        // Then
        verify(accountRepository).getAccount(accountId);
        assertEquals(BigDecimal.valueOf(900), account.getBalance());
        verify(accountRepository).updateAccount(account);
    }

    @Test
    void addOperation_should_throw_exception_when_account_not_found_for_deposit() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation depositOperation = new Operation(OperationType.CREDIT, amount);

        when(accountRepository.getAccount(accountId)).thenReturn(null);

        // When & Then
        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.addOperation(accountId, depositOperation);
        });

        assertEquals("Account not found", exception.getMessage());

    }

    @Test
    void addOperation_should_throw_exception_when_account_not_found_for_withdrawal() {
        // Given
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Operation withdrawalOperation = new Operation(OperationType.DEBIT, amount);

        when(accountRepository.getAccount(accountId)).thenReturn(null);

        // When & Then
        Exception exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.addOperation(accountId, withdrawalOperation);
        });

        assertEquals("Account not found", exception.getMessage());

    }

    @Test
    void generateMonthlyStatement_shouldReturnCorrectStatement_whenCalled() {
        // Given
        Long accountId = 1L;
        ZonedDateTime zonedNow = ZonedDateTime.now();
        ZonedDateTime unMoisAvant = zonedNow.minusMonths(1);
        Instant now = zonedNow.toInstant();
        Instant oneMonthAgo = unMoisAvant.toInstant();

        Operation operation1 = new Operation(OperationType.CREDIT, BigDecimal.valueOf(100));
        operation1.setDate(oneMonthAgo.plusSeconds(1));

        Operation operation2 = new Operation(OperationType.DEBIT, BigDecimal.valueOf(50));
        operation2.setDate(now.minusSeconds(1));

        Operation operation3 = new Operation(OperationType.CREDIT, BigDecimal.valueOf(200));
        operation3.setDate(oneMonthAgo.minusSeconds(1));

        Operation operation4 = new Operation(OperationType.DEBIT, BigDecimal.valueOf(25));
        operation4.setDate(now.plusSeconds(1));

        BankAccount bankAccount = new BankAccount(BigDecimal.valueOf(1000), BigDecimal.valueOf(1000),
                BankAccountType.CURRENT, BigDecimal.valueOf(2000));
        bankAccount.setOperations(Arrays.asList(operation1, operation2, operation3, operation4));
        bankAccount.setAccountType(BankAccountType.CURRENT);
        bankAccount.setBalance(BigDecimal.valueOf(500));

        when(accountRepository.getAccount(accountId)).thenReturn(bankAccount);

        // When
        AccountStatement statement = accountService.generateMonthlyStatement(accountId);

        // Then
        List<Operation> expectedOperations = Arrays.asList(operation2, operation1);
        assertEquals(BankAccountType.CURRENT, statement.getAccountType());
        assertEquals(BigDecimal.valueOf(500), statement.getBalance());
        assertEquals(expectedOperations, statement.getOperations());
    }

    @Test
    void generateMonthlyStatement_should_return_statement_with_no_operations_if_none_in_last_month() {
        // Given
        Long accountId = 1L;
        BankAccount account = new BankAccount(BigDecimal.valueOf(1000), BigDecimal.valueOf(200),
                BankAccountType.CURRENT, BigDecimal.valueOf(100));
        account.setId(accountId);

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime twoMonthsAgo = now.minusMonths(2);

        Operation operation1 = new Operation(OperationType.CREDIT, BigDecimal.valueOf(100));
        operation1.setDate(twoMonthsAgo.plusSeconds(1).toInstant());

        account.setOperations(Arrays.asList(operation1));

        when(accountRepository.getAccount(accountId)).thenReturn(account);

        // When
        AccountStatement statement = accountService.generateMonthlyStatement(accountId);

        // Then
        assertEquals(BankAccountType.CURRENT, statement.getAccountType());
        assertEquals(BigDecimal.valueOf(1000), statement.getBalance());
        assertTrue(statement.getOperations().isEmpty());
    }

}
