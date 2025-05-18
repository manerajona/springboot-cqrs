package com.github.manerajona.cqrs.domain.repositories;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.errors.DepositStatusCannotBeUpdatedException;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;

import java.util.UUID;
import java.util.function.BiPredicate;

public interface DepositRepository {
    void save(Deposit deposit);

    void updateStatus(UUID guid, DepositStatus status, BiPredicate<DepositStatus, DepositStatus> depositStatusInvariants) throws DepositNotFoundException, DepositStatusCannotBeUpdatedException;
}
