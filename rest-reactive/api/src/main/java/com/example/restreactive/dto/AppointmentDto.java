package com.example.restreactive.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AppointmentDto implements DtoObject {
    Integer id;
    String appointmentCode;
    StoreDto store;
    StoreSlotDto storeSlot;
    List<UserDto> users;
}
