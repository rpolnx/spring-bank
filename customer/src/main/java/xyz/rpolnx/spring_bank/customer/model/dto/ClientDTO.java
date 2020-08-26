package xyz.rpolnx.spring_bank.customer.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.customer.model.PersonType;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class ClientDTO {
    private String documentNumber;
    private String fullName;
    private PersonType personType;
    private int score;

    public ClientDTO(Client client) {
        this.documentNumber = client.getDocumentNumber();
        this.fullName = client.getFullName();
        this.personType = client.getPersonType();
        this.score = client.getScore();
    }

    public static ClientDTO of(Client client) {
        return new ClientDTO(client);
    }
}
