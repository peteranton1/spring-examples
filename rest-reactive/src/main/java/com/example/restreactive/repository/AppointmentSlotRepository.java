package com.example.restreactive.repository;

import com.example.restreactive.model.AppointmentSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {
}
