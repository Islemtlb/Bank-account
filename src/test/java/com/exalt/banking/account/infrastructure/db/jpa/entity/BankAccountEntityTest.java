
package com.exalt.banking.account.infrastructure.db.jpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.exalt.banking.account.domain.exceptions.InsufficientFundsException;
import com.exalt.banking.account.domain.model.BankAccount;

@ExtendWith(MockitoExtension.class)
class BankAccountEntityTest {

    @Test
    void deposit_should_add_amount_to_balance() {
        // Given
        BankAccount account = new BankAccount(1L, BigDecimal.valueOf(50), BigDecimal.valueOf(50));
        BigDecimal depositAmount = BigDecimal.valueOf(75);

        // When
        account.deposit(depositAmount);

        // Then
        assertEquals(BigDecimal.valueOf(125), account.getBalance());
    }

    @Test
    void withdraw_should_throw_InsufficientFundsException_when_amount_exceeds_balance_and_overdraft() {
        // Given
        BankAccount account = new BankAccount(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        BigDecimal withdrawalAmount = BigDecimal.valueOf(175);

        // When
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(withdrawalAmount));

        // Then
        assertEquals(BigDecimal.valueOf(100), account.getBalance());
    }

    @Test
    void withdraw_should_deduct_amount_from_balance_and_overdraft_when_sufficient_funds() {
        // Given
        BankAccount account = new BankAccount(1L, BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        BigDecimal withdrawalAmount = BigDecimal.valueOf(125);

        // When
        account.withdraw(withdrawalAmount);

        // Then
        assertEquals(BigDecimal.valueOf(-25), account.getBalance());
    }

    @Test
    void withdraw_should_not_deduct_from_overdraft_when_balance_is_sufficient() {
        // Given
        BankAccount account = new BankAccount(1L, BigDecimal.valueOf(200), BigDecimal.valueOf(100));
        BigDecimal withdrawalAmount = BigDecimal.valueOf(150);

        // When
        account.withdraw(withdrawalAmount);

        // Then
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
        assertEquals(BigDecimal.valueOf(100), account.getOverdraftLimit());
    }

    @ParameterizedTest
    @CsvSource({
            "true, 100.0, 50.0, 100.0, 50.0",
            "false, 100.0, 50.0, 200.0, 50.0",
            "false, 100.0, 50.0, 100.0, 60.0",
    })
    void equals_and_hashCode_should_work_correctly(boolean expectedEquals, BigDecimal balance1, BigDecimal overdraft1,
            BigDecimal balance2, BigDecimal overdraft2) {
        // Given
        BankAccount account1 = new BankAccount(1L, balance1, overdraft1);
        BankAccount account2 = new BankAccount(1L, balance2, overdraft2);

        // Then
        assertEquals(expectedEquals, account1.equals(account2));
        if (expectedEquals) {
            assertEquals(account1.hashCode(), account2.hashCode());
        }
    }

}