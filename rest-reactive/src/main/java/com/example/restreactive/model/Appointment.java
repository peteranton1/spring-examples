package com.example.restreactive.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "appt_appointment")
public record Appointment(
    @Id @GeneratedValue
    Long id,
    @OneToOne
    Store store,
    @OneToOne
    AppointmentSlot slot,
    @OneToMany
    List<User> users
) implements EntityObject {
}
