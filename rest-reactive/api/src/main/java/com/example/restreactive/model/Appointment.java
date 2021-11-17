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
    Integer id;

    String appointmentCode;

    @OneToOne
    Store store;

    @OneToOne
    StoreSlot storeSlot;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    List<User> users;
}
