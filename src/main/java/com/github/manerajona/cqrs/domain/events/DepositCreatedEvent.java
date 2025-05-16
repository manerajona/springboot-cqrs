package com.github.manerajona.cqrs.domain.events;

import com.github.manerajona.cqrs.domain.DomainEvent;
import com.github.manerajona.cqrs.domain.entities.Deposit;

public record DepositCreatedEvent(Deposit deposit) implements DomainEvent {
}
