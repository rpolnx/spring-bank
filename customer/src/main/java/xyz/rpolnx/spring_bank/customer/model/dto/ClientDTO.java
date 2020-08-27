package xyz.rpolnx.spring_bank.customer.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.rpolnx.spring_bank.customer.exceptions.BadRequestException;
import xyz.rpolnx.spring_bank.customer.model.PersonType;
import xyz.rpolnx.spring_bank.customer.model.entity.Client;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class ClientDTO {
    @NotNull
    @Pattern(regexp = "^\\d{11,14}$")
    private String documentNumber;
    @NotNull
    @NotEmpty
    private String fullName;
    @NotNull
    private PersonType personType;
    private Integer score;

    public ClientDTO(Client client) {
        this.documentNumber = client.getDocumentNumber();
        this.fullName = client.getFullName();
        this.personType = client.getPersonType();
        this.score = client.getScore();
    }

    public void validate() {
        int documentSize = this.getPersonType().getDocumentSize();

        if (documentNumber.length() != documentSize) {
            throw new BadRequestException(personType + " must has document size of " + documentSize);
        }
    }

    public static ClientDTO of(Client client) {
        return new ClientDTO(client);
    }
}
