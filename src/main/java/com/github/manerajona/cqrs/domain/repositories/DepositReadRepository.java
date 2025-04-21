package com.github.manerajona.cqrs.domain.repositories;

import com.github.manerajona.cqrs.domain.projections.DepositProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepositReadRepository {
    Optional<DepositProjection> findByGuid(UUID guid);

    List<DepositProjection> findAll();
}
