package com.github.manerajona.cqrs.domain;

import com.github.manerajona.cqrs.domain.commands.CreateDepositCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositStatusCommand;
import com.github.manerajona.cqrs.domain.entities.Currency;
import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.entities.DepositStatus;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.ports.output.mongo.DepositDao;
import com.github.manerajona.cqrs.ports.output.publisher.DepositEventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DepositAggregate {

    private final DepositDao depositDao;
    private final DepositEventProducer depositEventProducer;
    private final Currency defaultCurrency;

    public DepositAggregate(DepositDao depositDao, DepositEventProducer depositEventProducer,
                            @Value("${com.github.manerajona.deposits.default-currency}") String currency) {
        this.depositDao = depositDao;
        this.depositEventProducer = depositEventProducer;
        this.defaultCurrency = Currency.valueOf(currency);
    }

    public UUID handle(CreateDepositCommand command) {
        final Deposit deposit = createDepositCommandToDeposit(command);
        depositDao.save(deposit);
        depositEventProducer.publish(deposit);
        return deposit.guid();
    }

    public void handle(UpdateDepositStatusCommand command) throws DepositNotFoundException {
        depositDao.updateStatus(command.guid(), command.status());
    }

    private Deposit createDepositCommandToDeposit(CreateDepositCommand command) {
        return Deposit.builder()
                .guid(UUID.randomUUID())
                .accountNumber(command.accountNumber())
                .fullName(command.firstName() + " " + command.lastName())
                .amount(command.amount())
                .currency(defaultCurrency)
                .status(DepositStatus.PENDING)
                .build();
    }
}
