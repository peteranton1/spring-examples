package com.example.restreactive.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "appt_appointment_slot")
public class AppointmentSlot implements EntityObject {
    @Id
    @GeneratedValue
    Integer id;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
