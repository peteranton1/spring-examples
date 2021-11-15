package com.example.restreactive.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmailAddressDto implements DtoObject {
    Integer id;
    String email;
}
