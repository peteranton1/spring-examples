package com.example.restreactive.repository;

import com.example.restreactive.model.StreetAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StreetAddressRepository extends JpaRepository<StreetAddress, Long> {

    List<StreetAddress> findByLine1AndPostcode(String line1, String postcode);
}
