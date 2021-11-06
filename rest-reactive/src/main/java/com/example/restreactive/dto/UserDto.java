package com.example.restreactive.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class UserDto implements DtoObject {
    Long id;
    String username;
    String firstName;
    String lastName;
    EmailAddressDto email;
}
