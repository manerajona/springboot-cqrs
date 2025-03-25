package com.github.manerajona.cqrs.domain.errors;

public class DepositNotFoundException extends AbstractDomainException {
    public DepositNotFoundException() {
        super("Deposit not found");
    }
}
