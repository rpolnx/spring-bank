package xyz.rpolnx.spring_bank.account.external;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.rpolnx.spring_bank.account.model.entity.Account;
import xyz.rpolnx.spring_bank.account.model.enums.AccountStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    @Override
    List<Account> findAll();

    Optional<Account> findFirstByClientIdAndAgencyAndStatusIn(String clientId, String agency, List<AccountStatus> status);
}
