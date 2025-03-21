package com.github.manerajona.cqrs.ports.output.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Builder
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Document(collection = "accounts")
class DepositDocument {
    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();
    @Setter
    private int accountNumber;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private double amount;
}
