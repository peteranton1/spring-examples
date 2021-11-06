package com.example.restreactive.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appt_email_address")
public record EmailAddress(
    @Id @GeneratedValue
    Long id,
    String email
) implements EntityObject {
}
