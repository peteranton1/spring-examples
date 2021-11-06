package com.example.restreactive.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "appt_country")
public record Country(
    @Id @GeneratedValue
    Long id,
    String name,
    String code
) implements EntityObject {
}
