package com.example.restreactive.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "appt_user")
public class User implements EntityObject {
    @Id
    @GeneratedValue
    Long id;
    String username;
    String firstName;
    String lastName;
    @OneToOne
    EmailAddress email;
}
