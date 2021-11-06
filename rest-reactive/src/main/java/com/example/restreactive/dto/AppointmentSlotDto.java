package com.example.restreactive.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class AppointmentSlotDto implements DtoObject {
    Long id;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
