package com.github.manerajona.cqrs.ports.output.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "guid")
@Document(collection = "deposit_history")
class DepositHistoryDoc {
    @Id
    UUID guid;
    int accountNumber;
    double amount;
    String currency;
    @Singular("statusHistoryEntry")
    List<StatusHistoryEntry> statusHistory;

    public List<StatusHistoryEntry> getStatusHistory() {
        return Collections.unmodifiableList(statusHistory);
    }

    @Value
    @RequiredArgsConstructor
    public static class StatusHistoryEntry {
        String status;
        LocalDateTime timestamp = LocalDateTime.now();
    }
}