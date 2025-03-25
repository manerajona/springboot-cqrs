package com.github.manerajona.cqrs.ports.input.rest;

import com.github.manerajona.cqrs.domain.DepositAggregate;
import com.github.manerajona.cqrs.domain.DepositProjection;
import com.github.manerajona.cqrs.domain.commands.CreateDepositCommand;
import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.queries.FindAllDepositsQuery;
import com.github.manerajona.cqrs.domain.queries.FindDepositByGuidQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("deposits")
@RequiredArgsConstructor
public class DepositController {

    private final DepositAggregate aggregate;
    private final DepositProjection projection;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> saveDeposit(@Valid @RequestBody CreateDepositCommand request) {
        final UUID guid = aggregate.handle(request);
        return ResponseEntity.created(URI.create("/deposits/" + guid)).build();
    }

    @GetMapping
    public ResponseEntity<List<Deposit>> queryAll() {
        final List<Deposit> body = projection.handle(new FindAllDepositsQuery());
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/{guid}")
    public ResponseEntity<Deposit> queryByGuid(@PathVariable String guid) {
        final Deposit body = projection.handle(new FindDepositByGuidQuery(UUID.fromString(guid)));
        return ResponseEntity.ok().body(body);
    }
}
