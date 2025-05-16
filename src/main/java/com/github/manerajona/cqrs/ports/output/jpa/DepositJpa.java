package com.github.manerajona.cqrs.ports.output.jpa;

import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "guid")
@Table(name = "deposit")
@Entity
class DepositJpa {
    @Id
    //@UuidGenerator
    private UUID guid;

    @Column(nullable = false, updatable = false)
    private int accountNumber;

    @Column(nullable = false, updatable = false)
    private double amount;

    @Column(nullable = false, updatable = false)
    private Currency currency;

    @With
    private DepositStatus status;

    @Version
    @Column(nullable = false)
    private int version;

    public DepositJpa(UUID guid, int accountNumber, double amount, Currency currency, DepositStatus status) {
        this.guid = guid;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
    }
}