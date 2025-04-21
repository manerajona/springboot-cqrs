package com.github.manerajona.cqrs.ports.input.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.manerajona.cqrs.domain.DepositService;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositStatusCommand;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.events.DepositCreatedEvent;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositEventListener {

    private final ObjectMapper objectMapper;
    private final DepositService service;

    public void handleMessage(String message) {
        try {
            log.info("New event received: {}", message);
            Optional.of(objectMapper.readValue(message, DepositCreatedEvent.class))
                    .map(DepositEventListener::depositCreatedEventToUpdateDepositStatusCommand)
                    .ifPresent(service::handle);
            log.info("Deposit completed successfully");
        } catch (IOException ioException) {
            log.error("Error occurred while deserializing message", ioException);
        } catch (DepositNotFoundException notFoundException) {
            log.error(notFoundException.getMessage(), notFoundException);
        }
    }

    private static UpdateDepositStatusCommand depositCreatedEventToUpdateDepositStatusCommand(DepositCreatedEvent event) {
        return UpdateDepositStatusCommand.builder()
                .depositId(event.depositId())
                .status(DepositStatus.COMPLETED)
                .build();
    }
}
