package xyz.rpolnx.spring_bank.customer.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.common.model.enums.EventType;
import xyz.rpolnx.spring_bank.customer.model.PersonType;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
public class ClientEvent {
    @JsonProperty("clientId")
    private String documentNumber;
    private PersonType personType;
    private Integer score;
    private EventType type;

    public static ClientEvent of(Client client, EventType type) {
        return new ClientEvent(client.getDocumentNumber(), client.getPersonType(), client.getScore(), type);
    }
}
