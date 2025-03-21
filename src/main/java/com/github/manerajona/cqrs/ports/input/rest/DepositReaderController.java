package com.github.manerajona.cqrs.ports.input.rest;

import com.github.manerajona.cqrs.core.Deposit;
import com.github.manerajona.cqrs.ports.output.mongo.DepositDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("deposits")
@RequiredArgsConstructor
public class DepositReaderController {

    private final DepositDao depositDao;

    @GetMapping(value = "/deposits/{accountNumber}")
    public ResponseEntity<Deposit> findByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok().body(this.depositDao.handleByAccountNumber(accountNumber));
    }

    @GetMapping(value = "/deposits")
    public ResponseEntity<List<Deposit>> findAll() {
        return ResponseEntity.ok().body(this.depositDao.handleAll());
    }
}
