package com.github.manerajona.cqrs.domain.entities;

import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.domain.vo.Money;
import lombok.Value;

import java.util.UUID;

@Value
public class Deposit {
    DepositId id;
    Integer accountNumber;
    Money money;
    DepositStatus status;

    public Deposit(Integer accountNumber, Money money) {
        this.id = new DepositId(UUID.randomUUID());
        this.accountNumber = accountNumber;
        this.money = money;
        this.status = DepositStatus.PENDING;
    }
}