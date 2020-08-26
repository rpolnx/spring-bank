package xyz.rpolnx.spring_bank.customer.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, String> {

    @Override
    List<Client> findAll();
}
