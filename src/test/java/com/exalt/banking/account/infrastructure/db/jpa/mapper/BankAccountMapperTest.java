package com.exalt.banking.account.infrastructure.db.jpa.mapper;

import org.junit.jupiter.api.Test;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.infrastructure.db.jpa.entity.BankAccountEntity;
import com.exalt.banking.account.infrastructure.db.mapper.BankAccountMapper;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BankAccountMapperTest {

    @Test
    void toDomain_should_map_expected_fields() {
        // Given
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal overdraftLimit = new BigDecimal("50.00");
        BankAccountEntity entity = new BankAccountEntity(balance, overdraftLimit);
        BankAccount account = BankAccountMapper.toDomain(entity);

        // Then
        assertThat(account).isNotNull();
        assertThat(account.getBalance()).isEqualByComparingTo(balance);
        assertThat(account.getOverdraftLimit()).isEqualByComparingTo(overdraftLimit);
    }

    @Test
    void toDomain_should_return_null_when_entity_is_null() {
        // Given
        BankAccount account = BankAccountMapper.toDomain(null);

        // Then
        assertThat(account).isNull();
    }

    @Test
    void toEntity_should_map_expected_fields() {
        // Given
        BigDecimal balance = new BigDecimal("100.00");
        BigDecimal overdraftLimit = new BigDecimal("50.00");
        BankAccount account = new BankAccount(1L, balance, overdraftLimit);
        BankAccountEntity entity = BankAccountMapper.toEntity(account);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getBalance()).isEqualByComparingTo(balance);
        assertThat(entity.getOverdraftLimit()).isEqualByComparingTo(overdraftLimit);
    }

    @Test
    void toEntity_should_return_null_when_account_is_null() {
        // Given
        BankAccountEntity entity = BankAccountMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

}
