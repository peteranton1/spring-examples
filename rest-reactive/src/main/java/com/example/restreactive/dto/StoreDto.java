package com.example.restreactive.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StoreDto implements DtoObject {
    Long id;
    String storeName;
    String storeCode;
    StreetAddressDto address;
}
