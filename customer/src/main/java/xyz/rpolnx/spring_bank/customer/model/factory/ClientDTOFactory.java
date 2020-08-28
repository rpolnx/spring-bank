package xyz.rpolnx.spring_bank.customer.model.factory;

import xyz.rpolnx.spring_bank.common.model.dto.CustomerDTO;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

public class ClientDTOFactory {
    public static CustomerDTO fromEntity(Client client) {
        return new CustomerDTO(client.getDocumentNumber(), client.getFullName(), client.getPersonType(), client.getScore());
    }
}
