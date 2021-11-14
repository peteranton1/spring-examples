package com.example.restreactive.repository;

import com.example.restreactive.model.StoreSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface StoreSlotRepository extends JpaRepository<StoreSlot, Long> {

    List<StoreSlot> findAllSlotsByStoreCode(String storeCode);

    List<StoreSlot> findAllSlotsByStoreCodeAndSlotCode(String storeCode, String slotCode);

    @Query("SELECT s FROM StoreSlot s " +
        "WHERE s.startTime >= :startTime " +
        "AND s.startTime <= :endTime " +
        "ORDER BY s.startTime, s.endTime ASC")
    List<StoreSlot> findAllSlotsBetweenStartTimeAndEndTime(
        @Param("startTime") ZonedDateTime startTime,
        @Param("endTime") ZonedDateTime endTime
    );

    @Query("SELECT s FROM StoreSlot s " +
        "WHERE s.startTime >= :startTime " +
        "AND s.startTime <= :endTime " +
        "AND s.storeCode = :storeCode " +
        "ORDER BY s.startTime, s.endTime ASC")
    List<StoreSlot> findAllSlotsByStoreBetweenStartTimeAndEndTime(
        @Param("startTime") ZonedDateTime startTime,
        @Param("endTime") ZonedDateTime endTime,
        @Param("storeCode") String storeCode
        );

    @Query("SELECT s FROM StoreSlot s " +
        "WHERE s.startTime >= :startTime " +
        "AND s.startTime <= :endTime " +
        "AND s.storeCode IN (:storeCodes) " +
        "ORDER BY s.startTime, s.endTime ASC")
    List<StoreSlot> findAllSlotsByStoresListBetweenStartTimeAndEndTime(
        @Param("startTime") ZonedDateTime startTime,
        @Param("endTime") ZonedDateTime endTime,
        @Param("storeCodes") List<String> storeCode
        );

}
