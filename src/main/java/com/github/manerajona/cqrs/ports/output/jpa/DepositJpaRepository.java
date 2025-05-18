package com.github.manerajona.cqrs.ports.output.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface DepositJpaRepository extends JpaRepository<DepositJpa, UUID> {

}
