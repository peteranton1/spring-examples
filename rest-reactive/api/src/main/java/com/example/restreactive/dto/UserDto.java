package com.example.restreactive.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDto implements DtoObject {
    Integer id;
    String username;
    String firstName;
    String lastName;
    EmailAddressDto email;
}
