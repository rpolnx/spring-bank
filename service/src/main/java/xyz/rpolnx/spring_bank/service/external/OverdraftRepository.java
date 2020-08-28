package xyz.rpolnx.spring_bank.service.external;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;
import xyz.rpolnx.spring_bank.service.model.entity.Overdraft;

import java.util.List;
import java.util.Optional;

@Repository
public interface OverdraftRepository extends CrudRepository<Overdraft, Long> {
    @Override
    List<Overdraft> findAll();

    List<Overdraft> findAllByAccountIdAndDeletedOnIsNull(Long accountId);

    Optional<Overdraft> findByAccountIdAndDeletedOnIsNull(Long accountId);
}
