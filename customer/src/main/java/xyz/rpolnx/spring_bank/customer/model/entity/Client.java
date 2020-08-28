package xyz.rpolnx.spring_bank.customer.model.entity;

import lombok.*;
import xyz.rpolnx.spring_bank.common.model.entity.BaseEntity;
import xyz.rpolnx.spring_bank.common.model.enums.CustomPersonType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
@Data
@With
public class Client extends BaseEntity {
    @Id
    private String documentNumber;
    private String fullName;
    @Enumerated(EnumType.STRING)
    private CustomPersonType personType;
    private Integer score;

    public Client withNewValues(String fullName, CustomPersonType personType, Integer score) {
        this.fullName = fullName;
        this.personType = personType;
        this.score = score;
        return this;
    }
}
