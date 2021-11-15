package com.example.restreactive.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class StreetAddressDto implements DtoObject {
    private Integer id;
    private String line1;
    private String line2;
    private String city;
    private String county;
    private CountryDto country;
    private String postcode;
}
