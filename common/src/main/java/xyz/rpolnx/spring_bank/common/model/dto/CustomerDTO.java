package xyz.rpolnx.spring_bank.common.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import xyz.rpolnx.spring_bank.common.exceptions.BadRequestException;
import xyz.rpolnx.spring_bank.common.model.enums.CustomPersonType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class CustomerDTO {
    @NotNull
    @Pattern(regexp = "^\\d{11,14}$")
    @With
    private String documentNumber;
    @NotNull
    @NotEmpty
    private String fullName;
    @NotNull
    private CustomPersonType personType;
    private Integer score;

    public CustomerDTO(String documentNumber, String fullName, CustomPersonType personType) {
        this.documentNumber = documentNumber;
        this.fullName = fullName;
        this.personType = personType;
    }

    public void validate() {
        int documentSize = this.getPersonType().getDocumentSize();

        if (documentNumber.length() != documentSize) {
            throw new BadRequestException(personType + " must has document size of " + documentSize);
        }
    }

    public static CustomerDTO withOnlyDocumentNumber(String documentNumber) {
        return new CustomerDTO().withDocumentNumber(documentNumber);
    }
}
