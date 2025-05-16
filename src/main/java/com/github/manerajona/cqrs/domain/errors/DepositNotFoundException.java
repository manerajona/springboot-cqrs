package com.github.manerajona.cqrs.domain.errors;

public class DepositNotFoundException extends DomainException {
    public DepositNotFoundException() {
        super("Deposit not found");
    }
}
