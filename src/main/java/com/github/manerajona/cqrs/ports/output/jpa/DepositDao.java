package com.github.manerajona.cqrs.ports.output.jpa;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DepositDao {

    private final DepositRepository depositRepository;

    public void save(Deposit deposit) {
        var depositJpa = new DepositJpa(
                deposit.id().guid(),
                deposit.accountNumber(),
                deposit.money().amount(),
                deposit.money().currency(),
                deposit.status());
        depositRepository.save(depositJpa);
    }

    public void updateStatus(UUID guid, DepositStatus status) throws DepositNotFoundException {
        var depositJpa = depositRepository.findById(guid)
                .orElseThrow(DepositNotFoundException::new)
                .withStatus(status);
        depositRepository.save(depositJpa);
    }
}
