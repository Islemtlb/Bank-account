package com.exalt.banking.account.infrastructure.db.jpa.entity;

import com.exalt.banking.account.domain.model.OperationType;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "OPERATION")
public class OperationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    private BigDecimal amount;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    private Instant date;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccountEntity bankAccount;

    public OperationEntity() {
    }

    public OperationEntity(OperationType type, BigDecimal amount, BankAccountEntity bankAccount) {
        this.type = type;
        this.amount = amount;
        this.bankAccount = bankAccount;
    }

    public OperationEntity(OperationType type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OperationEntity that = (OperationEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(version, that.version) &&
                Objects.equals(date, that.date) &&
                Objects.equals(bankAccount, that.bankAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, amount, version, date, bankAccount);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountEntity bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }
}