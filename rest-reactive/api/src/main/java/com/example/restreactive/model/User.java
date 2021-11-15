package com.example.restreactive.model;

import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.EntityObject;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "appt_user")
public class User implements EntityObject {
    @Id
    @GeneratedValue
    Integer id;

    String username;
    String firstName;
    String lastName;
    @OneToOne
    EmailAddress email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Appointment appointment;
}
