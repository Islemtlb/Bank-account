
package com.exalt.banking.account.infrastructure.db.jpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BankAccountEntityTest {

    @Test
    void equals_should_return_true_for_same_object() {
        // Given
        BankAccountEntity entity = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);

        // When
        boolean result = entity.equals(entity);

        // Then
        assertTrue(result);
    }

    @Test
    void equals_should_return_false_for_null() {
        // Given
        BankAccountEntity entity = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);

        // When
        boolean result = entity.equals(null);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_should_return_false_for_different_class() {
        // Given
        BankAccountEntity entity = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        Object otherObject = new Object();

        // When
        boolean result = entity.equals(otherObject);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_should_return_false_for_different_ids() {
        // Given
        BankAccountEntity entity1 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity1.setId(1L);
        BankAccountEntity entity2 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity2.setId(2L);

        // When
        boolean result = entity1.equals(entity2);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_should_return_true_for_same_ids() {
        // Given
        BankAccountEntity entity1 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity1.setId(1L);
        BankAccountEntity entity2 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity2.setId(1L);

        // When
        boolean result = entity1.equals(entity2);

        // Then
        assertTrue(result);
    }

    @Test
    void hashCode_should_be_equal_for_same_ids() {
        // Given
        BankAccountEntity entity1 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity1.setId(1L);
        BankAccountEntity entity2 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity2.setId(1L);

        // When
        int hash1 = entity1.hashCode();
        int hash2 = entity2.hashCode();

        // Then
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCode_should_not_be_equal_for_different_ids() {
        // Given
        BankAccountEntity entity1 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity1.setId(1L);
        BankAccountEntity entity2 = new BankAccountEntity(BigDecimal.TEN, BigDecimal.ZERO, "CURRENT", BigDecimal.ZERO);
        entity2.setId(2L);

        // When
        int hash1 = entity1.hashCode();
        int hash2 = entity2.hashCode();

        // Then
        assertNotEquals(hash1, hash2);
    }
}
