package com.example.restreactive.service;

import com.example.restreactive.dto.AppointmentSlotDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.AppointmentSlot;
import com.example.restreactive.repository.AppointmentSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
public class AppointmentSlotService {

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AppointmentSlotDto> findAllSlots() {
        return appointmentSlotRepository.findAll()
            .stream()
            .map(slot -> (AppointmentSlotDto) modelMapper.toDto(slot))
            .toList();
    }

    public List<AppointmentSlotDto> findByStartTimeAndEndTime(ZonedDateTime startTime,
                                                              ZonedDateTime endTime) {
        return appointmentSlotRepository
            .findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(startTime, endTime)
            .stream()
            .map(slot -> (AppointmentSlotDto) modelMapper.toDto(slot))
            .toList();
    }


    public Long upsertAppointmentSlot(AppointmentSlotDto appointmentSlotDto) {
        requireNonNull(appointmentSlotDto);
        requireNonNull(appointmentSlotDto.getStartTime());
        requireNonNull(appointmentSlotDto.getEndTime());
        AppointmentSlot slotOut = appointmentSlotRepository.save(appointmentSlotRepository
            .findByStartTimeAndEndTime(
                appointmentSlotDto.getStartTime(),
                appointmentSlotDto.getEndTime()
            ).stream().findAny()
            .map(slot -> (AppointmentSlot) modelMapper.update(slot, appointmentSlotDto))
            .orElse((AppointmentSlot) modelMapper.insert(appointmentSlotDto))
        );
        return slotOut.getId();
    }
}
