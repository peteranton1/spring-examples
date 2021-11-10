package com.example.restreactive.repository;

import com.example.restreactive.model.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

    List<AppointmentSlot> findByStartTimeAndEndTime(ZonedDateTime startTime,
                                                    ZonedDateTime endTime);

    List<AppointmentSlot> findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
        ZonedDateTime startTime, ZonedDateTime endTime);
}
