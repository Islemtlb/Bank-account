package com.exalt.banking.account.infrastructure.db.jpa.repository;

import com.exalt.banking.account.domain.model.OperationType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.Operation;
import com.exalt.banking.account.infrastructure.db.jpa.entity.BankAccountEntity;
import com.exalt.banking.account.infrastructure.db.mapper.BankAccountMapper;

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
        BankAccountEntity accountEntity = new BankAccountEntity(balance, overdraftLimit);
        when(jpaAccountRepository.findById(accountId)).thenReturn(Optional.of(accountEntity));

        // When
        BankAccount account = accountRepository.getAccount(accountId);

        // Then
        assertEquals(balance, account.getBalance());
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
        BankAccount bankAccount = new BankAccount(1L, balance, overdraftLimit);

        BankAccountEntity expectedEntity = new BankAccountEntity(balance, overdraftLimit);
        expectedEntity.setId(expectedId);
        ArgumentCaptor<BankAccountEntity> argumentCaptor = ArgumentCaptor.forClass(BankAccountEntity.class);
        when(jpaAccountRepository.save(argumentCaptor.capture())).thenReturn(expectedEntity);

        // When
        long createdAccountId = accountRepository.createAccount(bankAccount);

        // Then
        verify(jpaAccountRepository).save(argumentCaptor.capture());
        assertEquals(expectedId, createdAccountId);
    }

    @Test
    void updateAccount_should_call_jpa_repository() {
        // Given
        BigDecimal newBalance = BigDecimal.valueOf(2000);
        BigDecimal overdraftLimit = BigDecimal.valueOf(1000);
        BankAccount account = new BankAccount(1L, newBalance, overdraftLimit);
        BankAccountEntity entity = BankAccountMapper.toEntity(account);
        Operation operation = new Operation(OperationType.CREDIT, BigDecimal.TEN);

        // When
        accountRepository.updateAccount(account, operation);

        // Then
        verify(jpaAccountRepository).save(entity);
    }
}
