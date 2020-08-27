package xyz.rpolnx.spring_bank.customer.external;

import xyz.rpolnx.spring_bank.customer.model.dto.ClientEvent;

public interface ClientPublisher {
    void handleClientCreation(ClientEvent clientDTO);
}
