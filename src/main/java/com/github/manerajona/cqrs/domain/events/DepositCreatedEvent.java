package com.github.manerajona.cqrs.domain.events;

import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.Money;
import lombok.Builder;

@Builder
public record DepositCreatedEvent(
        DepositId depositId,
        Integer accountNumber,
        Money money
) implements DomainEvent {
}
