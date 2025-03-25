package com.github.manerajona.cqrs.domain.queries;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FindDepositByGuidQuery(@NotNull UUID guid) {
}
