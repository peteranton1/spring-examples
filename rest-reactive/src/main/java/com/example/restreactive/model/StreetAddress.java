package com.example.restreactive.model;

import javax.persistence.*;

@Entity
@Table(name = "appt_street_address")
public record StreetAddress(
    @Id @GeneratedValue
    Long id,
    String line1,
    String line2,
    String city,
    String county,
    @OneToOne
    Country country,
    String postcode
) implements EntityObject {
}
