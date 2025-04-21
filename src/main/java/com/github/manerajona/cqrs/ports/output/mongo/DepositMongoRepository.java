package com.github.manerajona.cqrs.ports.output.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

interface DepositMongoRepository extends MongoRepository<DepositDocument, UUID> {

    Optional<DepositDocument> findByGuid(UUID guid);
}
