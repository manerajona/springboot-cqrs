package com.github.manerajona.cqrs.domain.projections;

import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import lombok.Builder;

@Builder
public record DepositProjection(int account, double amount, Currency currency, DepositStatus status) {
}
