package com.example.restreactive.repository;

import com.example.restreactive.model.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {

    Optional<EmailAddress> findByEmail(String email);
}
