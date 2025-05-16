package com.github.manerajona.cqrs.domain.queries;

import com.github.manerajona.cqrs.domain.vo.DepositId;
import jakarta.validation.constraints.NotNull;

public record FindDepositByGuidQuery(@NotNull DepositId depositId) {
}
