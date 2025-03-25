package com.github.manerajona.cqrs.domain;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.queries.FindAllDepositsQuery;
import com.github.manerajona.cqrs.domain.queries.FindDepositByGuidQuery;
import com.github.manerajona.cqrs.ports.output.mongo.DepositDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepositProjection {

    private final DepositDao depositDao;

    @Transactional(readOnly = true)
    public List<Deposit> handle(FindAllDepositsQuery ignored) {
        return depositDao.findAll();
    }

    @Transactional(readOnly = true)
    public Deposit handle(FindDepositByGuidQuery query) throws DepositNotFoundException {
        return depositDao.findByGuid(query.guid()).orElseThrow(DepositNotFoundException::new);
    }
}
