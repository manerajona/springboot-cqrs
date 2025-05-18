package com.github.manerajona.cqrs.domain.errors;

import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import lombok.Getter;

@Getter
public class DepositStatusCannotBeUpdatedException extends DomainException {

    private final DepositStatus currentStatus;
    private final DepositStatus targetStatus;

    public DepositStatusCannotBeUpdatedException(DepositStatus currentStatus, DepositStatus targetStatus) {
        super("Deposit status cannot be updated.");
        this.currentStatus = currentStatus;
        this.targetStatus = targetStatus;
    }
}
