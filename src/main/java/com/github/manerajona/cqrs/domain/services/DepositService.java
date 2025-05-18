package com.github.manerajona.cqrs.domain.services;

import com.github.manerajona.cqrs.domain.commands.CreateDepositCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositStatusCommand;
import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DomainException;
import com.github.manerajona.cqrs.domain.events.DepositCreatedEvent;
import com.github.manerajona.cqrs.domain.events.DepositStatusUpdatedEvent;
import com.github.manerajona.cqrs.domain.repositories.DepositRepository;
import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.domain.vo.Money;
import com.github.manerajona.cqrs.ports.output.publisher.DepositEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiPredicate;

@Service
public class DepositService {

    private final DepositRepository depositRepository;
    private final DepositEventPublisher depositEventPublisher;
    private final Currency defaultCurrency;

    private static final BiPredicate<DepositStatus, DepositStatus> depositStatusInvariants = (current, target) ->
            current.equals(DepositStatus.PENDING) && (target.equals(DepositStatus.COMPLETED) || target.equals(DepositStatus.ERROR));

    public DepositService(DepositRepository depositRepository, DepositEventPublisher depositEventPublisher,
                          @Value("${com.github.manerajona.deposits.default-currency}") Currency defaultCurrency) {
        this.depositRepository = depositRepository;
        this.depositEventPublisher = depositEventPublisher;
        this.defaultCurrency = defaultCurrency;
    }

    @Transactional
    public DepositId handle(CreateDepositCommand command) {
        var deposit = new Deposit(command.accountNumber(), new Money(command.amount(), defaultCurrency));
        depositRepository.save(deposit);

        var event = new DepositCreatedEvent(deposit);
        depositEventPublisher.publish(event);

        return deposit.getId();
    }

    @Transactional
    public void handle(UpdateDepositStatusCommand command) throws DomainException {
        depositRepository.updateStatus(command.depositId().guid(), command.status(), depositStatusInvariants);

        var event = new DepositStatusUpdatedEvent(command.depositId(), command.status());
        depositEventPublisher.publish(event);
    }
}
