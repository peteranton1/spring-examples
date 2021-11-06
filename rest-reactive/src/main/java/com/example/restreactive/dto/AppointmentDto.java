package com.example.restreactive.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class AppointmentDto implements DtoObject {
    Long id;
    StoreDto store;
    AppointmentSlotDto slot;
    List<UserDto> users;
}
