package com.github.manerajona.cqrs.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Deposit {
    @Positive
    private Integer accountNumber;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Positive
    private Double amount;
}
