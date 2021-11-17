package com.example.restreactive.repository;

import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StoreSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAppointmentCode(String appointmentCode);

    List<Appointment> findByStoreAndStoreSlot(Store store,
                                              StoreSlot slot);


    @Query("SELECT a FROM Appointment a " +
        "LEFT OUTER JOIN a.storeSlot ss " +
        "WHERE ss.startTime >= :startTime " +
        "AND ss.startTime <= :endTime " +
        "ORDER BY ss.startTime, ss.endTime ASC")
    List<Appointment> findAllAppointmentsBetweenStartTimeAndEndTime(
        @Param("startTime") ZonedDateTime startTime,
        @Param("endTime") ZonedDateTime endTime
    );

    @Query("SELECT a FROM Appointment a " +
        "LEFT OUTER JOIN a.storeSlot ss " +
        "LEFT OUTER JOIN a.store s " +
        "WHERE ss.startTime >= :startTime " +
        "AND ss.startTime <= :endTime " +
        "AND s.storeCode IN (:storeCodes) " +
        "ORDER BY ss.startTime, ss.endTime ASC")
    List<Appointment> findAllAppointmentsByStoresListBetweenStartTimeAndEndTime(
        @Param("startTime") ZonedDateTime startTime,
        @Param("endTime") ZonedDateTime endTime,
        @Param("storeCodes") List<String> storeCode
    );

}
