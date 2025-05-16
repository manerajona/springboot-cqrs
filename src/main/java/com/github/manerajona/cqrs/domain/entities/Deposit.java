package com.github.manerajona.cqrs.domain.entities;

import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.domain.vo.Money;

public record Deposit(
        DepositId id,
        Integer accountNumber,
        Money money,
        DepositStatus status
) {
}