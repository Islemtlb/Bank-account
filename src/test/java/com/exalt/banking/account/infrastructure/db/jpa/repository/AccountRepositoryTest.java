package com.exalt.banking.account.infrastructure.db.jpa.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.BankAccountType;
import com.exalt.banking.account.infrastructure.db.jpa.entity.BankAccountEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {
    @Mock
    private JpaAccountRepository jpaAccountRepository;

    @InjectMocks
    private AccountRepositoryAdaptor accountRepository;

    @BeforeEach
    public void setUp() {
        accountRepository = new AccountRepositoryAdaptor(jpaAccountRepository);
    }

    @Test
    void getAccount_should_return_account_when_found() {
        // Given
        Long accountId = 1L;
        BigDecimal balance = BigDecimal.valueOf(1000);
        BigDecimal overdraftLimit = BigDecimal.valueOf(1000);
        String accountType = "CURRENT";
        BigDecimal savingsDepositLimit = BigDecimal.valueOf(2000);

        BankAccountEntity accountEntity = new BankAccountEntity(balance, overdraftLimit, accountType,
                savingsDepositLimit);
        when(jpaAccountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));

        // When
        BankAccount account = accountRepository.getAccount(accountId);

        // Then
        assertEquals(balance, account.getBalance());
        assertEquals(overdraftLimit, account.getOverdraftLimit());
        assertEquals(BankAccountType.CURRENT, account.getAccountType());
        assertEquals(savingsDepositLimit, account.getSavingsDepositLimit());
    }

    @Test
    void getAccount_should_throw_exception_when_account_not_found() {
        // Given
        Long accountId = 1L;
        when(jpaAccountRepository.findById(accountId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(AccountNotFoundException.class, () -> accountRepository.getAccount(accountId));
    }

    @Test
    void createAccount_should_return_id_and_call_jpa_repository() {
        // Given
        Long expectedId = 1L;
        BigDecimal balance = BigDecimal.valueOf(1000);
        BigDecimal overdraftLimit = BigDecimal.valueOf(1000);
        BankAccountType accountType = BankAccountType.CURRENT;
        BigDecimal savingsDepositLimit = BigDecimal.valueOf(2000);

        BankAccount bankAccount = new BankAccount(balance, overdraftLimit, accountType, savingsDepositLimit);

        BankAccountEntity expectedEntity = new BankAccountEntity(balance, overdraftLimit, accountType.name(),
                savingsDepositLimit);
        expectedEntity.setId(expectedId);
        ArgumentCaptor<BankAccountEntity> argumentCaptor = ArgumentCaptor.forClass(BankAccountEntity.class);
        when(jpaAccountRepository.save(argumentCaptor.capture())).thenReturn(expectedEntity);

        // When
        long createdAccountId = accountRepository.createAccount(bankAccount);

        // Then
        verify(jpaAccountRepository).save(argumentCaptor.capture());
        assertEquals(expectedId, createdAccountId);
    }
}