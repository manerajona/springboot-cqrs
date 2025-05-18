package com.github.manerajona.cqrs.ports.output.jpa;

import com.github.manerajona.cqrs.domain.entities.Deposit;
import com.github.manerajona.cqrs.domain.errors.DepositNotFoundException;
import com.github.manerajona.cqrs.domain.errors.DepositStatusCannotBeUpdatedException;
import com.github.manerajona.cqrs.domain.repositories.DepositRepository;
import com.github.manerajona.cqrs.domain.vo.DepositStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.function.BiPredicate;

@Repository
@RequiredArgsConstructor
class DepositDao implements DepositRepository {

    private final DepositJpaRepository depositRepository;

    @Override
    public void save(Deposit deposit) {
        var depositJpa = DepositJpa.builder()
                .guid(deposit.getId().guid())
                .accountNumber(deposit.getAccountNumber())
                .amount(deposit.getMoney().amount())
                .currency(deposit.getMoney().currency())
                .status(deposit.getStatus())
                .build();
        depositRepository.save(depositJpa);
    }

    @Override
    public void updateStatus(UUID guid, DepositStatus status, BiPredicate<DepositStatus, DepositStatus> depositStatusInvariants)
            throws DepositNotFoundException, DepositStatusCannotBeUpdatedException {
        var depositJpa = depositRepository.findById(guid).orElseThrow(DepositNotFoundException::new);

        DepositStatus currentStatus = depositJpa.getStatus();
        if (depositStatusInvariants.test(currentStatus, status)) {
            depositRepository.save(depositJpa.withStatus(status));
        } else {
            throw new DepositStatusCannotBeUpdatedException(currentStatus, status);
        }
    }
}
