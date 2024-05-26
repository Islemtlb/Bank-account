package com.exalt.banking.account.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;

import com.exalt.banking.account.domain.exceptions.DepositCeilingExceededException;
import com.exalt.banking.account.domain.exceptions.InsufficientFundsException;

class BankAccountTest {

        @Test
        void deposit_should_add_amount_to_balance() {
                // Given
                BankAccount account = new BankAccount(BigDecimal.valueOf(50), BigDecimal.valueOf(50),
                                BankAccountType.CURRENT,
                                BigDecimal.valueOf(200));
                BigDecimal depositAmount = BigDecimal.valueOf(75);

                // When
                account.deposit(depositAmount);

                // Then
                assertEquals(BigDecimal.valueOf(125), account.getBalance());
        }

        @Test
        void withdraw_should_throw_InsufficientFundsException_when_amount_exceeds_balance_and_overdraft() {
                // Given
                BankAccount account = new BankAccount(BigDecimal.valueOf(100), BigDecimal.valueOf(50),
                                BankAccountType.CURRENT,
                                BigDecimal.ZERO);
                BigDecimal withdrawalAmount = BigDecimal.valueOf(175);

                // When
                assertThrows(InsufficientFundsException.class, () -> account.withdraw(withdrawalAmount));

                // Then
                assertEquals(BigDecimal.valueOf(100), account.getBalance());
        }

        @Test
        void withdraw_should_deduct_amount_from_balance_and_overdraft_when_sufficient_funds() {
                // Given
                BankAccount account = new BankAccount(BigDecimal.valueOf(100), BigDecimal.valueOf(50),
                                BankAccountType.CURRENT,
                                BigDecimal.ZERO);
                BigDecimal withdrawalAmount = BigDecimal.valueOf(125);

                // When
                account.withdraw(withdrawalAmount);

                // Then
                assertEquals(BigDecimal.valueOf(-25), account.getBalance());
        }

        @Test
        void withdraw_should_not_deduct_from_overdraft_when_balance_is_sufficient() {
                // Given
                BankAccount account = new BankAccount(BigDecimal.valueOf(200), BigDecimal.valueOf(100),
                                BankAccountType.CURRENT, BigDecimal.ZERO);
                BigDecimal withdrawalAmount = BigDecimal.valueOf(150);

                // When
                account.withdraw(withdrawalAmount);

                // Then
                assertEquals(BigDecimal.valueOf(50), account.getBalance());
                assertEquals(BigDecimal.valueOf(100), account.getOverdraftLimit());
        }

        @ParameterizedTest
        @CsvSource({
                        "true, 100.0, 50.0, CURRENT, 200.0, 100.0, 50.0, CURRENT, 200.0",
                        "false, 100.0, 50.0, CURRENT, 200.0, 200.0, 50.0, CURRENT, 200.0",
                        "false, 100.0, 50.0, CURRENT, 200.0, 100.0, 60.0, CURRENT, 200.0",
                        "false, 100.0, 50.0, CURRENT, 200.0, 100.0, 50.0, SAVINGS, 200.0"
        })
        void equals_and_hashCode_should_work_correctly(boolean expectedEquals, BigDecimal balance1,
                        BigDecimal overdraft1,
                        @ConvertWith(BankAccountTypeConverter.class) BankAccountType type1, BigDecimal savingsLimit1,
                        BigDecimal balance2, BigDecimal overdraft2,
                        @ConvertWith(BankAccountTypeConverter.class) BankAccountType type2, BigDecimal savingsLimit2) {
                // Given
                BankAccount account1 = new BankAccount(balance1, overdraft1, type1, savingsLimit1);
                BankAccount account2 = new BankAccount(balance2, overdraft2, type2, savingsLimit2);

                // Then
                assertEquals(expectedEquals, account1.equals(account2));
                if (expectedEquals) {
                        assertEquals(account1.hashCode(), account2.hashCode());
                }
        }

        @Test
        void deposit_should_throw_exception_when_savings_deposit_exceeds_limit() {
                // Given
                BigDecimal balance = BigDecimal.valueOf(150);
                BigDecimal overdraftLimit = BigDecimal.ZERO;
                BankAccountType accountType = BankAccountType.SAVINGS;
                BigDecimal savingsDepositLimit = BigDecimal.valueOf(200);
                BigDecimal depositAmount = BigDecimal.valueOf(75);
                BankAccount account = new BankAccount(balance, overdraftLimit, accountType, savingsDepositLimit);

                // When / Then
                DepositCeilingExceededException exception = assertThrows(DepositCeilingExceededException.class,
                                () -> account.deposit(depositAmount));
                assertEquals("Dépassement du plafond de dépôt autorisé.", exception.getMessage());

                assertEquals(balance, account.getBalance());
        }
}
