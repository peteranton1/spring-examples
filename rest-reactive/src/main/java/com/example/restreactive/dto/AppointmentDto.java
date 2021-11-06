package com.example.restreactive.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AppointmentDto implements DtoObject {
    Long id;
    StoreDto store;
    AppointmentSlotDto slot;
    List<UserDto> users;
}
