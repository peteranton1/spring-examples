package com.example.restreactive.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AppointmentSlotDto implements DtoObject {
    Integer id;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
