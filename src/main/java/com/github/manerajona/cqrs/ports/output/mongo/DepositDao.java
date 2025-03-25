package com.github.manerajona.cqrs.ports.output.mongo;

import com.github.manerajona.cqrs.domain.entities.Currency;
import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.entities.DepositStatus;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.ports.output.mongo.DepositDocument.StatusHistoryEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepositDao {

    private final DepositRepository depositRepository;

    @Transactional
    public void save(Deposit deposit) {
        final DepositDocument document = mapEntityToDocument(deposit);
        depositRepository.save(document);
    }

    @Transactional
    public void updateStatus(UUID guid, DepositStatus status) throws DepositNotFoundException {
        final DepositDocument document = depositRepository.findById(guid)
                .orElseThrow(DepositNotFoundException::new)
                .toBuilder()
                .statusHistoryEntry(new StatusHistoryEntry(status.name()))
                .build();
        depositRepository.save(document);
    }

    @Transactional(readOnly = true)
    public Optional<Deposit> findByGuid(UUID guid) {
        return depositRepository.findByGuid(guid).map(DepositDao::mapDocumentToEntity);
    }

    @Transactional(readOnly = true)
    public List<Deposit> findAll() {
        return depositRepository.findAll().stream()
                .map(DepositDao::mapDocumentToEntity)
                .toList();
    }

    private static Deposit mapDocumentToEntity(DepositDocument document) {
        final StatusHistoryEntry lastStatusEntry = document.getStatusHistory().getLast();
        return Deposit.builder()
                .guid(document.getGuid())
                .accountNumber(document.getAccountNumber())
                .fullName(document.getFullName())
                .amount(document.getAmount())
                .currency(Currency.valueOf(document.getCurrency()))
                .status(DepositStatus.valueOf(lastStatusEntry.getStatus()))
                .build();
    }

    private static DepositDocument mapEntityToDocument(Deposit deposit) {
        final StatusHistoryEntry statusHistoryEntry = new StatusHistoryEntry(deposit.status().name());
        return DepositDocument.builder()
                .guid(deposit.guid())
                .accountNumber(deposit.accountNumber())
                .fullName(deposit.fullName())
                .amount(deposit.amount())
                .currency(deposit.currency().name())
                .statusHistoryEntry(statusHistoryEntry)
                .build();
    }
}
