package com.github.manerajona.cqrs.domain.errors;

public class DomainException extends RuntimeException {
    DomainException(String message) {
        super(message);
    }
}
