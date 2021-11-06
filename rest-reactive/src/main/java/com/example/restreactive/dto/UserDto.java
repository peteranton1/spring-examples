package com.example.restreactive.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto implements DtoObject {
    Long id;
    String username;
    String firstName;
    String lastName;
    EmailAddressDto email;
}
