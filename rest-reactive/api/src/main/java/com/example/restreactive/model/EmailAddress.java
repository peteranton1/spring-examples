package com.example.restreactive.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "appt_email_address")
public class EmailAddress implements EntityObject {
    @Id
    @GeneratedValue
    Integer id;
    String email;

}
