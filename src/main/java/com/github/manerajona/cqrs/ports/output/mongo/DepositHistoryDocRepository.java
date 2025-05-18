package com.github.manerajona.cqrs.ports.output.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

interface DepositHistoryDocRepository extends MongoRepository<DepositHistoryDoc, UUID> {

    Optional<DepositHistoryDoc> findByGuid(UUID guid);
}
