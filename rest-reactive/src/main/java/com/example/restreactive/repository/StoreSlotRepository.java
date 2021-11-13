package com.example.restreactive.repository;

import com.example.restreactive.model.StoreSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;

public interface StoreSlotRepository extends JpaRepository<StoreSlot, Long> {

    List<StoreSlot> findAllSlotsBySlotCode(String slotCode);

    @Query("SELECT s FROM StoreSlot s " +
        "WHERE s.startTime >= ?1 " +
        "AND s.startTime <= ?2 " +
        "ORDER BY s.startTime, s.endTime ASC")
    List<StoreSlot> findAllSlotsBetweenStartTimeAndEndTime(
        ZonedDateTime startTime,
        ZonedDateTime endTime);

    @Query("SELECT s FROM StoreSlot s " +
        "WHERE s.startTime >= ?1 " +
        "AND s.startTime <= ?2 " +
        "AND s.storeCode = ?3 " +
        "ORDER BY s.startTime, s.endTime ASC")
    List<StoreSlot> findAllSlotsByStoreBetweenStartTimeAndEndTime(
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        String storeCode
        );
}
