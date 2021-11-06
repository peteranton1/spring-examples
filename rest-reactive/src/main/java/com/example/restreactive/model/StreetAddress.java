package com.example.restreactive.model;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "appt_street_address")
public class StreetAddress implements EntityObject {
    @Id
    @GeneratedValue
    Long id;
    String line1;
    String line2;
    String city;
    String county;
    @OneToOne
    Country country;
    String postcode;
}
