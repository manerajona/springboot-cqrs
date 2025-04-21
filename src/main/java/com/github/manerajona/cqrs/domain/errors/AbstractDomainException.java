package com.github.manerajona.cqrs.domain.errors;

abstract class AbstractDomainException extends RuntimeException {
    AbstractDomainException(String message) {
        super(message);
    }
}
