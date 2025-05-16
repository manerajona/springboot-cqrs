package com.github.manerajona.cqrs.domain.commands;

import jakarta.validation.constraints.Positive;

public record CreateDepositCommand(
        @Positive Integer accountNumber,
        @Positive Double amount
) {
}
