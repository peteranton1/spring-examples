package com.example.restreactive.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class StoreDto implements DtoObject {
    Long id;
    String storeName;
    String storeCode;
    StreetAddressDto address;
}
