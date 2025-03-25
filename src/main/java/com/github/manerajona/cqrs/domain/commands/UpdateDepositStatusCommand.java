package com.github.manerajona.cqrs.domain.commands;

import com.github.manerajona.cqrs.domain.entities.DepositStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateDepositStatusCommand(
        @NotNull UUID guid,
        @NotNull DepositStatus status
) {
}
