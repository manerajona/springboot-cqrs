package com.github.manerajona.cqrs.ports.output.mongo;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.projections.DepositStatusProjection;
import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.ports.output.mongo.DepositHistoryDoc.StatusHistoryEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepositHistoryDao {

    private final DepositHistoryRepository depositRepository;

    public void save(Deposit deposit) {
        Optional.ofNullable(deposit)
                .map(DepositHistoryDao::depositToDepositDocument)
                .ifPresent(depositRepository::save);
    }

    public void updateStatus(UUID guid, DepositStatus status) throws DepositNotFoundException {
        final DepositHistoryDoc document = depositRepository.findById(guid)
                .orElseThrow(DepositNotFoundException::new)
                .toBuilder()
                .statusHistoryEntry(new StatusHistoryEntry(status.name()))
                .build();
        depositRepository.save(document);
    }

    public Optional<DepositProjection> findByGuid(UUID guid) {
        return depositRepository.findByGuid(guid).map(DepositHistoryDao::depositDocumentToDepositDetails);
    }

    public List<DepositProjection> findAll() {
        return depositRepository.findAll().stream()
                .map(DepositHistoryDao::depositDocumentToDepositDetails)
                .toList();
    }

    private static DepositProjection depositDocumentToDepositDetails(DepositHistoryDoc document) {
        DepositStatusProjection[] statusHistory = document.getStatusHistory().stream()
                .map(entry -> {
                    var status = DepositStatus.valueOf(entry.getStatus());
                    var timestamp = entry.getTimestamp();
                    return new DepositStatusProjection(status, timestamp);
                }).toArray(DepositStatusProjection[]::new);

        return DepositProjection.builder()
                .guid(document.getGuid())
                .account(document.getAccountNumber())
                .amount(document.getAmount())
                .currency(Currency.valueOf(document.getCurrency()))
                .statusHistory(statusHistory)
                .build();
    }

    private static DepositHistoryDoc depositToDepositDocument(Deposit deposit) {
        var statusHistoryEntry = new StatusHistoryEntry(deposit.status().name());
        return DepositHistoryDoc.builder()
                .guid(deposit.id().guid())
                .accountNumber(deposit.accountNumber())
                .amount(deposit.money().amount())
                .currency(deposit.money().currency().name())
                .statusHistoryEntry(statusHistoryEntry)
                .build();
    }
}
