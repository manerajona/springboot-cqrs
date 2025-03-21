package com.github.manerajona.cqrs.ports.output.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

interface DepositRepository extends MongoRepository<DepositDocument, String> {

    @Query("{'accountNumber': ?0}")
    Optional<DepositDocument> findByAccountNumber(String accountNumber);
}
