package com.github.manerajona.cqrs.ports.input.rest;

import com.github.manerajona.cqrs.core.Deposit;
import com.github.manerajona.cqrs.ports.output.publisher.DepositEventProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DepositWriterController {

    private final DepositEventProducer depositEventHandler;

    @PostMapping(value = "/deposits", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> saveDeposit(@Valid @RequestBody Deposit request) {
        return ResponseEntity.accepted().body(this.depositEventHandler.publish(request));
    }
}
