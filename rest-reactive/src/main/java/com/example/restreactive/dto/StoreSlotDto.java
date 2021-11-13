package com.example.restreactive.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StoreSlotDto implements DtoObject {
    Integer id;
    String slotCode;
    String storeCode;
    ZonedDateTime startTime;
    ZonedDateTime endTime;
}
