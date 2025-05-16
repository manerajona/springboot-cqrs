package com.github.manerajona.cqrs.domain.service;

import com.github.manerajona.cqrs.domain.commands.CreateDepositHistoryCommand;
import com.github.manerajona.cqrs.domain.commands.UpdateDepositHistoryStatusCommand;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.projections.DepositProjection;
import com.github.manerajona.cqrs.domain.queries.FindAllDepositsQuery;
import com.github.manerajona.cqrs.domain.queries.FindDepositByGuidQuery;
import com.github.manerajona.cqrs.ports.output.mongo.DepositHistoryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositHistoryService {

    private final DepositHistoryDao depositHistoryDao;

    @Transactional
    public void handle(CreateDepositHistoryCommand command) {
        depositHistoryDao.save(command.deposit());
    }

    @Transactional
    public void handle(UpdateDepositHistoryStatusCommand command) throws DepositNotFoundException {
        depositHistoryDao.updateStatus(command.depositId().guid(), command.status());
    }

    @Transactional(readOnly = true)
    public List<DepositProjection> handle(FindAllDepositsQuery ignored) {
        return depositHistoryDao.findAll();
    }

    @Transactional(readOnly = true)
    public DepositProjection handle(FindDepositByGuidQuery query) throws DepositNotFoundException {
        return depositHistoryDao.findByGuid(query.depositId().guid()).orElseThrow(DepositNotFoundException::new);
    }

}
