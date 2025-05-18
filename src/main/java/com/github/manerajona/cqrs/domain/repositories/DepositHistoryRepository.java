package com.github.manerajona.cqrs.domain.repositories;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;

import java.util.List;
import java.util.UUID;

public interface DepositHistoryRepository {
    void save(Deposit deposit);

    void putStatus(UUID guid, DepositStatus status) throws DepositNotFoundException;

    DepositProjection findByGuid(UUID guid) throws DepositNotFoundException;

    List<DepositProjection> findAll();
}
