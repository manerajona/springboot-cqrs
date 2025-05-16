package com.github.manerajona.cqrs.ports.input.rest;

import com.github.manerajona.cqrs.domain.commands.CreateDepositCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositStatusCommand;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.queries.FindAllDepositsQuery;
import com.github.manerajona.cqrs.domain.queries.FindDepositByGuidQuery;
import com.github.manerajona.cqrs.domain.service.DepositHistoryService;
import com.github.manerajona.cqrs.domain.service.DepositService;
import com.github.manerajona.cqrs.domain.vo.DepositId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("deposits")
@Slf4j
@RequiredArgsConstructor
public class DepositResource {

    private final DepositService depositService;
    private final DepositHistoryService depositHistoryService;

    @PostMapping
    ResponseEntity<Void> save(@Valid @RequestBody CreateDepositCommand command) {
        DepositId depositId = depositService.handle(command);
        return ResponseEntity.created(URI.create("/deposits/" + depositId.guid())).build();
    }

    @PatchMapping("/{guid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable UUID guid, @Valid @RequestBody UpdateDepositStatusCommand command) {
        DepositId depositId = new DepositId(guid);
        depositService.handle(command.withDepositId(depositId));
    }

    @GetMapping
    ResponseEntity<List<DepositProjection>> queryAll() {
        var query = new FindAllDepositsQuery();
        return ResponseEntity.ok().body(depositHistoryService.handle(query));
    }

    @GetMapping("/{guid}")
    ResponseEntity<DepositProjection> queryByGuid(@PathVariable UUID guid) {
        var query = new FindDepositByGuidQuery(new DepositId(guid));
        return ResponseEntity.ok().body(depositHistoryService.handle(query));
    }

    @ExceptionHandler(value = DepositNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handleAggregateNotFoundException(DepositNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException error) {
        log.error(error.getMessage(), error);
        Map<String, String> errors = error.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));
        return ResponseEntity.badRequest().body(errors);
    }
}
