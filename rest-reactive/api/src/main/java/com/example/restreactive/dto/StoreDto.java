package com.example.restreactive.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StoreDto implements DtoObject {
    Integer id;
    String storeName;
    String storeCode;
    StreetAddressDto address;
}
