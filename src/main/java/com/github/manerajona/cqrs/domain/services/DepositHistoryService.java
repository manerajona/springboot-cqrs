package com.github.manerajona.cqrs.domain.services;

import com.github.manerajona.cqrs.domain.commands.CreateDepositHistoryCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositHistoryStatusCommand;
import com.github.manerajona.cqrs.domain.errors.DomainException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.queries.FindAllDepositsQuery;
import com.github.manerajona.cqrs.domain.queries.FindDepositByGuidQuery;
import com.github.manerajona.cqrs.domain.repositories.DepositHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositHistoryService {

    private final DepositHistoryRepository depositHistoryRepository;

    @Transactional
    public void handle(CreateDepositHistoryCommand command) {
        depositHistoryRepository.save(command.deposit());
    }

    @Transactional
    public void handle(UpdateDepositHistoryStatusCommand command) throws DomainException {
        depositHistoryRepository.putStatus(command.depositId().guid(), command.status());
    }

    @Transactional(readOnly = true)
    public List<DepositProjection> handle(FindAllDepositsQuery ignored) {
        return depositHistoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DepositProjection handle(FindDepositByGuidQuery query) throws DomainException {
        return depositHistoryRepository.findByGuid(query.depositId().guid());
    }
}
