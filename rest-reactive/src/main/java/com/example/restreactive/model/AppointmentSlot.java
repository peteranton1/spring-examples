package com.example.restreactive.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "appt_appointment_slot")
public record AppointmentSlot(
    @Id @GeneratedValue
    Long id,
    ZonedDateTime startTime,
    ZonedDateTime endTime
) implements EntityObject {
}
