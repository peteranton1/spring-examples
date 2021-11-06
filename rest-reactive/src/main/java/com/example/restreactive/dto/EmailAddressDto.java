package com.example.restreactive.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmailAddressDto implements DtoObject {
    Long id;
    String email;
}
