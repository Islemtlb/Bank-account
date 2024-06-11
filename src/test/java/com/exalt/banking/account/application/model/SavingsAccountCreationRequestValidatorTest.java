package com.exalt.banking.account.application.model;

import com.exalt.banking.account.domain.model.BankAccountType;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SavingsAccountCreationRequestValidatorTest {

    @InjectMocks
    private SavingsAccountCreationRequestValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValid_with_non_savings_accountType_should_return_true() {
        // Given
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.CURRENT);

        // When
        boolean isValid = validator.isValid(request, context);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isValid_with_negative_savingsDepositLimit_should_return_false() {
        // Given
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.SAVINGS);
        when(request.savingsDepositLimit()).thenReturn(BigDecimal.valueOf(-100));

        // When
        boolean isValid = validator.isValid(request, context);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isValid_with_null_savingsDepositLimit_should_return_false() {
        // Given
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.SAVINGS);
        when(request.savingsDepositLimit()).thenReturn(null);

        // When
        boolean isValid = validator.isValid(request, context);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isValid_with_negative_balance_should_return_false() {
        // Given
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.SAVINGS);
        when(request.balance()).thenReturn(BigDecimal.valueOf(-50));

        // When
        boolean isValid = validator.isValid(request, context);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isValid_with_balance_exceeding_savingsDepositLimit_should_return_false() {
        // Given
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.SAVINGS);
        when(request.balance()).thenReturn(BigDecimal.valueOf(100));
        when(request.savingsDepositLimit()).thenReturn(BigDecimal.valueOf(50));

        // When
        boolean isValid = validator.isValid(request, context);

        // Then
        assertFalse(isValid);
    }

    @Test
    void isValid_with_valid_request_should_return_true() {
        // Given
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.SAVINGS);
        when(request.savingsDepositLimit()).thenReturn(BigDecimal.valueOf(100));

        // When
        boolean isValid = validator.isValid(request, context);

        // Then
        assertTrue(isValid);
    }

    @Test
    void isValid_with_valid_request_and_non_savings_accountType_should_return_true() {
        // Given
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.CURRENT);

        // When
        boolean isValid = validator.isValid(request, context);

        // Then
        assertTrue(isValid);
    }
}
