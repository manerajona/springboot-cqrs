package com.github.manerajona.cqrs.domain;

import com.github.manerajona.cqrs.domain.commands.CreateDepositCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositStatusCommand;
import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.events.DepositCreatedEvent;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.queries.FindAllDepositsQuery;
import com.github.manerajona.cqrs.domain.queries.FindDepositByGuidQuery;
import com.github.manerajona.cqrs.domain.repositories.DepositReadRepository;
import com.github.manerajona.cqrs.domain.repositories.DepositWriteRepository;
import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositId;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.domain.vo.Money;
import com.github.manerajona.cqrs.ports.output.publisher.DepositEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final DepositWriteRepository writeRepository;
    private final DepositReadRepository readRepository;
    private final DepositEventPublisher eventPublisher;
    private @Value("${com.github.manerajona.deposits.default-currency}") Currency defaultCurrency;

    @Transactional
    public UUID handle(CreateDepositCommand command) {
        final Deposit deposit = createDepositCommandToDeposit(command);
        writeRepository.save(deposit);

        final DepositCreatedEvent event = depositToDepositCreatedEvent(deposit);
        eventPublisher.publish(event);

        return deposit.id().guid();
    }

    @Transactional
    public void handle(UpdateDepositStatusCommand command) throws DepositNotFoundException {
        writeRepository.updateStatus(command.depositId().guid(), command.status());
    }

    @Transactional(readOnly = true)
    public List<DepositProjection> handle(FindAllDepositsQuery ignored) {
        return readRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DepositProjection handle(FindDepositByGuidQuery query) throws DepositNotFoundException {
        return readRepository.findByGuid(query.depositId().guid()).orElseThrow(DepositNotFoundException::new);
    }

    private Deposit createDepositCommandToDeposit(CreateDepositCommand command) {
        return Deposit.builder()
                .id(new DepositId(UUID.randomUUID()))
                .accountNumber(command.accountNumber())
                .money(new Money(command.amount(), defaultCurrency))
                .status(DepositStatus.PENDING)
                .build();
    }

    private static DepositCreatedEvent depositToDepositCreatedEvent(Deposit deposit) {
        return DepositCreatedEvent.builder()
                .depositId(deposit.id())
                .accountNumber(deposit.accountNumber())
                .money(deposit.money())
                .build();
    }
}
