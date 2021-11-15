package com.example.restreactive.repository;

import com.example.restreactive.model.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {

    List<EmailAddress> findByEmail(String email);
}
