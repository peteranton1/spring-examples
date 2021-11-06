package com.example.restreactive.service;

import com.example.restreactive.dto.AppointmentSlotDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.repository.AppointmentSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
