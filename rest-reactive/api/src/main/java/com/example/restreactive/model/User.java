package com.example.restreactive.model;

import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.EntityObject;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private List<Appointment> appointments;
}
