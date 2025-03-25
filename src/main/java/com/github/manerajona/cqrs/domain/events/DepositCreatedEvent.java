package com.github.manerajona.cqrs.domain.events;

import com.github.manerajona.cqrs.domain.entities.Currency;
import lombok.Builder;

import java.util.UUID;

@Builder
public record DepositCreatedEvent(
        UUID guid,
        Integer accountNumber,
        Double amount,
        Currency currency
) {
}
