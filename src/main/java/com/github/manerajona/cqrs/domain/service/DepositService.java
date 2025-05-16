package com.github.manerajona.cqrs.domain.service;

import com.github.manerajona.cqrs.domain.commands.CreateDepositCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositStatusCommand;
import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.events.DepositCreatedEvent;
import com.github.manerajona.cqrs.domain.events.DepositStatusUpdatedEvent;
import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.domain.vo.Money;
import com.github.manerajona.cqrs.ports.output.jpa.DepositDao;
import com.github.manerajona.cqrs.ports.output.publisher.DepositEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositDao depositDao;
    private final DepositEventPublisher depositEventPublisher;
    private @Value("${com.github.manerajona.deposits.default-currency}") Currency defaultCurrency;

    @Transactional
    public DepositId handle(CreateDepositCommand command) {
        var deposit = new Deposit(
                new DepositId(UUID.randomUUID()),
                command.accountNumber(),
                new Money(command.amount(), defaultCurrency),
                DepositStatus.PENDING);
        depositDao.save(deposit);

        var event = new DepositCreatedEvent(deposit);
        depositEventPublisher.publish(event);

        return deposit.id();
    }

    @Transactional
    public void handle(UpdateDepositStatusCommand command) throws DepositNotFoundException {
        depositDao.updateStatus(command.depositId().guid(), command.status());

        var event = new DepositStatusUpdatedEvent(command.depositId(), command.status());
        depositEventPublisher.publish(event);
    }
}
