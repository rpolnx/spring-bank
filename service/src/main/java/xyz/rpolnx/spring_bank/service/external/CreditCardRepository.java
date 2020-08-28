package xyz.rpolnx.spring_bank.service.external;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.rpolnx.spring_bank.service.model.entity.CreditCard;

import java.util.List;

@Repository
public interface CreditCardRepository extends CrudRepository<CreditCard, Long> {
    @Override
    List<CreditCard> findAll();

    List<CreditCard> findAllByAccountIdAndDeletedOnIsNull(Long accountId);
}
