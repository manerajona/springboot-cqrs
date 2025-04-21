package com.github.manerajona.cqrs.domain.events;

public interface DomainEventPublisher {
    void publish(DomainEvent domainEvent);
}
