package com.github.manerajona.cqrs.domain;

public interface DomainEventPublisher {
    void publish(DomainEvent domainEvent);
}
