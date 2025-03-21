package com.github.manerajona.cqrs.ports.output.mongo;

import com.github.manerajona.cqrs.core.Deposit;
import com.github.manerajona.cqrs.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepositDao {

    private final DepositRepository depositRepository;

    public Deposit handleByAccountNumber(String accountNumber) {
        return this.depositRepository.findByAccountNumber(accountNumber)
                .map(DepositDao::mapDocumentToRequest)
                .orElseThrow(NotFoundException::new);
    }

    public List<Deposit> handleAll() {
        return this.depositRepository.findAll().stream()
                .map(DepositDao::mapDocumentToRequest)
                .toList();
    }

    public UUID handleSave(Deposit deposit) {
        final DepositDocument document = mapEventToDocument(deposit);
        return this.depositRepository.save(document).getId();
    }

    private static Deposit mapDocumentToRequest(DepositDocument document) {
        return Deposit.builder()
                .accountNumber(document.getAccountNumber())
                .firstName(document.getFirstName())
                .lastName(document.getLastName())
                .amount(document.getAmount())
                .build();
    }

    private static DepositDocument mapEventToDocument(Deposit deposit) {
        return DepositDocument.builder()
                .accountNumber(deposit.getAccountNumber())
                .firstName(deposit.getFirstName())
                .lastName(deposit.getLastName())
                .amount(deposit.getAmount())
                .build();
    }
}
