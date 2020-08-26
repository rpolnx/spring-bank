package xyz.rpolnx.spring_bank.customer.service;

import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getAll();

    ClientDTO get(String documentNumber);

    ClientDTO create(ClientDTO client);

    void update(ClientDTO client, String documentNumber);

    void delete(String documentNumber);
}
