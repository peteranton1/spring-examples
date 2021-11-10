package com.example.restreactive.repository;

import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.AppointmentSlot;
import com.example.restreactive.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByStoreAndAppointmentSlot(Store store,
                                                    AppointmentSlot slot);
}
