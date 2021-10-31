package com.example.customerservices;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
}
