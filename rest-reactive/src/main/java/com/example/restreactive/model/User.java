package com.example.restreactive.model;

import javax.persistence.*;

@Entity
@Table(name = "appt_user")
public record User(
    @Id @GeneratedValue Long id,
    String username,
    String firstName,
    String lastName,
    @OneToOne
    EmailAddress email
) implements EntityObject {
}
