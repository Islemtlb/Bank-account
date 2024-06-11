package com.exalt.banking.account.application.model;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import com.exalt.banking.account.domain.model.BankAccountType;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CurrentAccountCreationRequestValidatorTest {

    @InjectMocks
    private CurrentAccountCreationRequestValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void isValid_with_non_current_account_should_return_true() {
        // Arrange
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.SAVINGS);

        // Act
        boolean isValid = validator.isValid(request, context);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_with_null_overdraftLimit_should_return_false() {
        // Arrange
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.CURRENT);
        when(request.overdraftLimit()).thenReturn(null);

        // Act
        boolean isValid = validator.isValid(request, context);

        // Assert
        assertFalse(isValid);

    }

    @Test
    void isValid_with_negative_overdraftLimit_should_return_false() {
        // Arrange
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.CURRENT);
        when(request.overdraftLimit()).thenReturn(BigDecimal.valueOf(-100));

        // Act
        boolean isValid = validator.isValid(request, context);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isValid_with_negative_balance_greater_than_overdraftLimit_should_return_false() {
        // Arrange
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.CURRENT);
        when(request.overdraftLimit()).thenReturn(BigDecimal.valueOf(100));
        when(request.balance()).thenReturn(BigDecimal.valueOf(-200));

        // Act
        boolean isValid = validator.isValid(request, context);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void isValid_with_negative_balance_less_than_overdraftLimit_should_return_true() {
        // Arrange
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.CURRENT);
        when(request.overdraftLimit()).thenReturn(BigDecimal.valueOf(100));
        when(request.balance()).thenReturn(BigDecimal.valueOf(-50));

        // Act
        boolean isValid = validator.isValid(request, context);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void isValid_with_positive_balance_should_return_true() {
        // Arrange
        BankAccountCreationRequest request = mock(BankAccountCreationRequest.class);
        when(request.accountType()).thenReturn(BankAccountType.CURRENT);
        when(request.overdraftLimit()).thenReturn(BigDecimal.valueOf(100));
        when(request.balance()).thenReturn(BigDecimal.valueOf(200));

        // Act
        boolean isValid = validator.isValid(request, context);

        // Assert
        assertTrue(isValid);
    }
}
