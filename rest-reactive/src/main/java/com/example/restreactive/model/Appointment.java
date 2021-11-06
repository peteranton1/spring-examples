package com.example.restreactive.model;

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
@Table(name = "appt_appointment")
public class Appointment implements EntityObject {
    @Id
    @GeneratedValue
    Long id;
    @OneToOne
    Store store;
    @OneToOne
    AppointmentSlot slot;
    @OneToMany
    List<User> users;
}
