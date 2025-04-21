package com.github.manerajona.cqrs.domain.repositories;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;

import java.util.UUID;

public interface DepositWriteRepository {
    void save(Deposit deposit);

    void updateStatus(UUID guid, DepositStatus status) throws DepositNotFoundException;
}
