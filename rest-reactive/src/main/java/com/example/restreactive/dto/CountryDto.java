package com.example.restreactive.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CountryDto implements DtoObject {
    Integer id;
    String name;
    String code;
}
