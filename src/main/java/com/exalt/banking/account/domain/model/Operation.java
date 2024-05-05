package com.exalt.banking.account.domain.model;

import java.math.BigDecimal;

public class Operation {

    private Long id;
    private OperationType type;
    private BigDecimal amount;

    public Operation(OperationType type, BigDecimal amount) {
        this.type = type;
        this.amount = amount;
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

}
