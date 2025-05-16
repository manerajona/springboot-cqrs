package com.github.manerajona.cqrs.domain.commands;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import jakarta.validation.constraints.NotNull;

public record CreateDepositHistoryCommand(@NotNull Deposit deposit) {
}
