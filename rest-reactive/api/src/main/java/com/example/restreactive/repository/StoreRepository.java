package com.example.restreactive.repository;

import com.example.restreactive.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByStoreCode(String storeCode);
}
