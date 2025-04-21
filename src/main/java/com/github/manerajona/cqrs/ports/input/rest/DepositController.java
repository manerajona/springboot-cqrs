package com.github.manerajona.cqrs.ports.input.rest;

import com.github.manerajona.cqrs.domain.DepositService;
import com.github.manerajona.cqrs.domain.commands.CreateDepositCommand;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.queries.FindAllDepositsQuery;
import com.github.manerajona.cqrs.domain.queries.FindDepositByGuidQuery;
import com.github.manerajona.cqrs.domain.vo.DepositId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private final DepositService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> saveDeposit(@Valid @RequestBody CreateDepositCommand request) {
        UUID guid = service.handle(request);
        return ResponseEntity.created(URI.create("/deposits/" + guid)).build();
    }

    @GetMapping
    public ResponseEntity<List<DepositProjection>> queryAll() {
        var query = new FindAllDepositsQuery();
        return ResponseEntity.ok().body(service.handle(query));
    }

    @GetMapping("/{guid}")
    public ResponseEntity<DepositProjection> queryByGuid(@PathVariable UUID guid) {
        var query = new FindDepositByGuidQuery(new DepositId(guid));
        return ResponseEntity.ok().body(service.handle(query));
    }

    @ExceptionHandler(value = DepositNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAggregateNotFoundException(DepositNotFoundException exception) {
        return exception.getMessage();
    }
}
