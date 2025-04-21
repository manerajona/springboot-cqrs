package com.github.manerajona.cqrs.ports.output.mongo;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.repositories.DepositReadRepository;
import com.github.manerajona.cqrs.domain.repositories.DepositWriteRepository;
import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.ports.output.mongo.DepositDocument.StatusHistoryEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepositDao implements DepositWriteRepository, DepositReadRepository {

    private final DepositMongoRepository depositRepository;

    @Override
    public void save(Deposit deposit) {
        Optional.ofNullable(deposit)
                .map(DepositDao::depositToDepositDocument)
                .ifPresent(depositRepository::save);
    }

    @Override
    public void updateStatus(UUID guid, DepositStatus status) throws DepositNotFoundException {
        final DepositDocument document = depositRepository.findById(guid)
                .orElseThrow(DepositNotFoundException::new)
                .toBuilder()
                .statusHistoryEntry(new StatusHistoryEntry(status.name()))
                .build();
        depositRepository.save(document);
    }

    @Override
    public Optional<DepositProjection> findByGuid(UUID guid) {
        return depositRepository.findByGuid(guid).map(DepositDao::depositDocumentToDepositDetails);
    }

    @Override
    public List<DepositProjection> findAll() {
        return depositRepository.findAll().stream()
                .map(DepositDao::depositDocumentToDepositDetails)
                .toList();
    }

    private static DepositProjection depositDocumentToDepositDetails(DepositDocument document) {
        final StatusHistoryEntry lastStatusEntry = document.getStatusHistory().getLast();
        return DepositProjection.builder()
                .account(document.getAccountNumber())
                .amount(document.getAmount())
                .currency(Currency.valueOf(document.getCurrency()))
                .status(DepositStatus.valueOf(lastStatusEntry.getStatus()))
                .build();
    }

    private static DepositDocument depositToDepositDocument(Deposit deposit) {
        final StatusHistoryEntry statusHistoryEntry = new StatusHistoryEntry(deposit.status().name());
        return DepositDocument.builder()
                .guid(deposit.id().guid())
                .accountNumber(deposit.accountNumber())
                .amount(deposit.money().amount())
                .currency(deposit.money().currency().name())
                .statusHistoryEntry(statusHistoryEntry)
                .build();
    }
}
