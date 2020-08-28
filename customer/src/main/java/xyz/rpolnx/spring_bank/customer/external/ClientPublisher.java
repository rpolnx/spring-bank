package xyz.rpolnx.spring_bank.customer.external;

import xyz.rpolnx.spring_bank.common.model.dto.CustomerEvent;

public interface ClientPublisher {
    void handleClientEvent(CustomerEvent event);
}
