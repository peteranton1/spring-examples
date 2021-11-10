package com.example.restreactive.repository;

import com.example.restreactive.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {

    List<Country> findByCode(String code);
}
