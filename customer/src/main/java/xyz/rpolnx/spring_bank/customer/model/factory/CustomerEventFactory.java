package xyz.rpolnx.spring_bank.customer.model.factory;

import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.common.model.enums.PersonType;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;
import xyz.rpolnx.spring_bank.customer.model.enums.CustomPersonType;

import java.util.Objects;

public class CustomerEventFactory {
    public static CustomerEvent generateCustomer(Client client, EventType type) {
        CustomerEvent.Customer innerCustomer = new CustomerEvent.Customer(client.getDocumentNumber(),
                getCommonEnum(client.getPersonType()), client.getScore(), client.getFullName());

        return new CustomerEvent(type, innerCustomer);
    }

    private static PersonType getCommonEnum(CustomPersonType personType) {
        return Objects.nonNull(personType) ? personType.toCommonPersonType() : null;
    }
}
