package com.example.restreactive.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class StreetAddressDto implements DtoObject {
    private Long id;
    private String line1;
    private String line2;
    private String city;
    private String county;
    private CountryDto country;
    private String postcode;
}
