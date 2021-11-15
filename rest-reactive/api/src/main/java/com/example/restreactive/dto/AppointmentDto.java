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
    StoreDto store;
    StoreSlotDto storeSlotDto;
    List<UserDto> users;
}
