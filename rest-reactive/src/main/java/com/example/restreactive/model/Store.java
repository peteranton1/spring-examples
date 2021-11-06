package com.example.restreactive.model;

import javax.persistence.*;

@Entity
@Table(name = "appt_store")
public record Store(
    @Id @GeneratedValue Long id,
    String storeName,
    String storeCode,
    @OneToOne
    StreetAddress address
) implements EntityObject {
}
