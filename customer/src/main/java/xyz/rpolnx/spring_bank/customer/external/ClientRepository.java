package xyz.rpolnx.spring_bank.customer.external;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

import java.util.List;

@Repository
public interface ClientRepository extends CrudRepository<Client, String> {

    @Override
    List<Client> findAll();
}
