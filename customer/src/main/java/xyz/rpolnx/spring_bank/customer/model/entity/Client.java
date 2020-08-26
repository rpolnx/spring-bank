package xyz.rpolnx.spring_bank.customer.model.entity;

import lombok.*;
import xyz.rpolnx.spring_bank.customer.model.PersonType;

import javax.persistence.Entity;
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
    private PersonType personType;
    private int score;
}