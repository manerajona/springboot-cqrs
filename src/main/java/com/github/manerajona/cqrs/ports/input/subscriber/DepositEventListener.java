package com.github.manerajona.cqrs.ports.input.subscriber;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.manerajona.cqrs.domain.commands.CreateDepositHistoryCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositHistoryStatusCommand;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.events.DepositCreatedEvent;
import com.github.manerajona.cqrs.domain.events.DepositStatusUpdatedEvent;
import com.github.manerajona.cqrs.domain.service.DepositHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositEventListener {

    private final ObjectMapper objectMapper;
    private final DepositHistoryService depositHistoryService;

    public void handleMessage(String message) {
        try {
            log.info("New event received: {}", message);
            JsonNode rootNode = objectMapper.readTree(message);
            String classCanonicalName = rootNode.has("@class") ? rootNode.get("@class").asText() : "";

            if (DepositCreatedEvent.class.getCanonicalName().equals(classCanonicalName)) {
                var event = objectMapper.treeToValue(rootNode, DepositCreatedEvent.class);
                var command = new CreateDepositHistoryCommand(event.deposit());
                depositHistoryService.handle(command);
            } else if (DepositStatusUpdatedEvent.class.getCanonicalName().equals(classCanonicalName)) {
                var event = objectMapper.treeToValue(rootNode, DepositStatusUpdatedEvent.class);
                var command = new UpdateDepositHistoryStatusCommand(event.depositId(), event.status());
                depositHistoryService.handle(command);
            } else {
                throw new UnsupportedOperationException(classCanonicalName + " is not a deposit event");
            }
            log.info("Deposit event processed successfully");
        } catch (IOException ioException) {
            log.error("Error occurred while deserializing message", ioException);
        } catch (DepositNotFoundException | UnsupportedOperationException exception) {
            log.error(exception.getMessage(), exception);
        }
    }
}
