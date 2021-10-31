package com.example.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface CustomerRepository extends
        RevisionRepository<Customer, Long, Integer>,
        JpaRepository<Customer, Long> {

    Collection<Customer> findByFirstAndLast(String f, String l);

    @Query("select c from Customer c " +
            "where c.first = :f and c.last = :l")
    Collection<Customer> byFullName(@Param("f") String f,
                                    @Param("l") String l);

    @Query(nativeQuery = true)
    Collection<OrderSummary> orderSummary();
}
