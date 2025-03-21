package com.github.manerajona.cqrs.ports.input.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.manerajona.cqrs.core.Deposit;
import com.github.manerajona.cqrs.ports.output.mongo.DepositDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DepositEventListener {

    private final ObjectMapper objectMapper;
    private final DepositDao depositDao;

    public void handle(String payload) {
        try {
            log.info("New event received: {}", payload);
            Deposit deposit = objectMapper.readValue(payload, Deposit.class);
            log.info("parsed event : {}", deposit);
            final UUID id = this.depositDao.handleSave(deposit);
            log.info("Saved deposit with id: {}", id);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
