package com.exalt.banking.account.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Operation {

    private Long id;
    private OperationType type;
    private BigDecimal amount;
    private Instant date;

    public Operation(OperationType type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
        date = Instant.now();
    }

    public OperationType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

}
