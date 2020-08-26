package xyz.rpolnx.spring_bank.customer.external;

import xyz.rpolnx.spring_bank.customer.model.dto.ClientDTO;

public interface ClientPublisher {
    void handleClientCreation(ClientDTO clientDTO);
}
