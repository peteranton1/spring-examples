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
@Table(name = "appt_store")
public class Store implements EntityObject {
    @Id
    @GeneratedValue
    Integer id;
    String storeName;
    String storeCode;
    @OneToOne
    StreetAddress address;
}
