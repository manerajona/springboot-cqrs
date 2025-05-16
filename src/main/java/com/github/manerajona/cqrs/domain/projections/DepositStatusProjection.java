package com.github.manerajona.cqrs.domain.projections;

import com.github.manerajona.cqrs.domain.vo.DepositStatus;

import java.time.LocalDateTime;

public record DepositStatusProjection(
        DepositStatus status,
        LocalDateTime timestamp) {
}
