package com.github.manerajona.cqrs.domain.projections;

import com.github.manerajona.cqrs.domain.vo.Currency;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DepositProjection(
        UUID guid,
        int account,
        double amount,
        Currency currency,
        DepositStatusProjection[] statusHistory) {
}
