package com.github.manerajona.cqrs.domain.events;

import com.github.manerajona.cqrs.domain.DomainEvent;
import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;

public record DepositStatusUpdatedEvent(DepositId depositId, DepositStatus status) implements DomainEvent {
}
