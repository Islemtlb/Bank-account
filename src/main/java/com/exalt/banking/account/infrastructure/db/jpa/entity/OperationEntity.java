package com.exalt.banking.account.infrastructure.db.jpa.entity;

import com.exalt.banking.account.domain.model.OperationType;

import jakarta.persistence.*;
import java.math.BigDecimal;

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
}