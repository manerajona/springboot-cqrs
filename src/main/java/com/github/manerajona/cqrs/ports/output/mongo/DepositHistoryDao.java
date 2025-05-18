package com.github.manerajona.cqrs.ports.output.mongo;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.projections.DepositStatusProjection;
import com.github.manerajona.cqrs.domain.repositories.DepositHistoryRepository;
import com.github.manerajona.cqrs.domain.vo.Currency;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import com.github.manerajona.cqrs.ports.output.mongo.DepositHistoryDoc.StatusHistoryEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class DepositHistoryDao implements DepositHistoryRepository {

    private final DepositHistoryDocRepository depositRepository;

    @Override
    public void save(Deposit deposit) {
        var document = DepositHistoryDoc.builder()
                .guid(deposit.getId().guid())
                .accountNumber(deposit.getAccountNumber())
                .amount(deposit.getMoney().amount())
                .currency(deposit.getMoney().currency().name())
                .statusHistoryEntry(new StatusHistoryEntry(deposit.getStatus().name()))
                .build();
        depositRepository.save(document);
    }

    @Override
    public void putStatus(UUID guid, DepositStatus status) throws DepositNotFoundException {
        var document = depositRepository.findById(guid)
                .orElseThrow(DepositNotFoundException::new)
                .toBuilder()
                .statusHistoryEntry(new StatusHistoryEntry(status.name()))
                .build();
        depositRepository.save(document);
    }

    @Override
    public DepositProjection findByGuid(UUID guid) throws DepositNotFoundException {
        return depositRepository.findByGuid(guid)
                .map(this::toDepositProjection)
                .orElseThrow(DepositNotFoundException::new);
    }

    @Override
    public List<DepositProjection> findAll() {
        return depositRepository.findAll().stream()
                .map(this::toDepositProjection)
                .toList();
    }

    private DepositProjection toDepositProjection(DepositHistoryDoc document) {
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
}
