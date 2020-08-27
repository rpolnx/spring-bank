package xyz.rpolnx.spring_bank.customer.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.customer.model.PersonType;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
public class ClientEvent {
    private String documentNumber;
    private PersonType personType;
    private Integer score;
    private Type type;

    public static ClientEvent of(Client client, ClientEvent.Type type) {
        return new ClientEvent(client.getDocumentNumber(), client.getPersonType(), client.getScore(), type);
    }

    public enum Type {CREATION, UPDATE}
}
