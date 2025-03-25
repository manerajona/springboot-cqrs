package com.github.manerajona.cqrs.domain.entities;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Deposit(
        UUID guid,
        Integer accountNumber,
        String fullName,
        Double amount,
        Currency currency,
        DepositStatus status
) {
}