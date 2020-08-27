package xyz.rpolnx.spring_bank.account.external;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.rpolnx.spring_bank.account.model.entity.Account;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    @Override
    List<Account> findAll();

    @Query(value = "INSERT INTO Account(number, clientId, agency, type, status)")
    Account createNewer(Account account);
}