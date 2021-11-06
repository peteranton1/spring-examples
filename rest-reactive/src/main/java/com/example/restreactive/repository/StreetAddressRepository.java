package com.example.restreactive.repository;

import com.example.restreactive.model.StreetAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetAddressRepository extends JpaRepository<StreetAddress, Long> {
}
