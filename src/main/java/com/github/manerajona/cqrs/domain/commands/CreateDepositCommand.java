package com.github.manerajona.cqrs.domain.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateDepositCommand(
        @Positive Integer accountNumber,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Positive Double amount
) {
}
