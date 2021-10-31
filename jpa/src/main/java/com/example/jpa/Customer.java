package com.example.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Audited
@NamedNativeQueries(
        @NamedNativeQuery(
                name = "Customer.orderSummary",
                "count(id) from orders " +
                "group by sku"
        )
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer extends MappedAuditableBase {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
    private String first;

    @Column(name = "last_name")
    private String last;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "customer_fk")
    private Set<Order> orders = new HashSet<>();
}
