package com.github.manerajona.cqrs.domain.commands;

import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import jakarta.validation.constraints.NotNull;
import lombok.With;

public record UpdateDepositStatusCommand(
        @With DepositId depositId,
        @NotNull DepositStatus status
) {
}
