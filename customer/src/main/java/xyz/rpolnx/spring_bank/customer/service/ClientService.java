package xyz.rpolnx.spring_bank.customer.service;

import xyz.rpolnx.spring_bank.common.model.dto.CustomerDTO;

import java.util.List;

public interface ClientService {
    List<CustomerDTO> getAll();

    CustomerDTO get(String documentNumber);

    CustomerDTO create(CustomerDTO client);

    void update(CustomerDTO client, String documentNumber);

    void delete(String documentNumber);
}
