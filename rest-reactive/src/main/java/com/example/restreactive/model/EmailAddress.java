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
@Entity
@Table(name = "appt_email_address")
public class EmailAddress implements EntityObject {
    @Id
    @GeneratedValue
    Long id;
    String email;

}
