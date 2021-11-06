package com.example.restreactive.repository;

import com.example.restreactive.model.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {
}
