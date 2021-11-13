package com.example.restreactive.service;

import com.example.restreactive.dto.AppointmentDto;
import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.AppointmentRepository;
import com.example.restreactive.repository.StoreSlotRepository;
import com.example.restreactive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Objects.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StoreSlotRepository storeSlotRepository;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AppointmentDto> findAllAppointments() {
        return appointmentRepository.findAll()
            .stream()
            .map(appointment -> (AppointmentDto) modelMapper.toDto(appointment))
            .toList();
    }

    public List<AppointmentDto> findByStoreAndAppointmentSlot(
        StoreDto storeDto, StoreSlotDto slotDto) {

        // Validate Input
        validateInputs(storeDto, slotDto);

        // Get Entities
        StoreSlot slot = getAppointmentSlot(
            slotDto.getStartTime(), slotDto.getEndTime());

        Store store = getStore(storeDto.getStoreCode());

        // Get Slot
        return appointmentRepository
            .findByStoreAndStoreSlot(store, slot)
            .stream()
            .map(appointment -> (AppointmentDto) modelMapper
                .toDto(appointment))
            .toList();
    }

    public Integer upsertAppointment(AppointmentDto appointmentDto) {
        // Validate Input
        requireNonNull(appointmentDto);
        StoreDto storeDto = appointmentDto.getStore();
        StoreSlotDto slotDto = appointmentDto.getStoreSlotDto();
        validateInputs(storeDto, slotDto);

        // Get Entities and ids
        StoreSlot slot = getAppointmentSlot(
            slotDto.getStartTime(), slotDto.getEndTime());
        appointmentDto.getStoreSlotDto().setId(slot.getId());

        Store store = getStore(storeDto.getStoreCode());
        appointmentDto.getStore().setId(store.getId());

        appointmentDto.getUsers().forEach(userDto -> {
            User user = userRepository.findByUsername(userDto.getUsername())
                .stream().findFirst()
                .orElseThrow(() -> new AppointmentException(String.format(
                    "Unable to find username: '%s'", userDto.getUsername()
                )));
            userDto.setId(user.getId());
        });

        // Get Slot
        Appointment apptOut = appointmentRepository.save(appointmentRepository
            .findByStoreAndStoreSlot(
                store,
                slot
            ).stream().findAny()
            .map(appointment -> (Appointment) modelMapper
                .update(appointment, appointmentDto))
            .orElse((Appointment) modelMapper.insert(appointmentDto))
        );
        return apptOut.getId();
    }

    StoreSlot getAppointmentSlot(ZonedDateTime startTime,
                                 ZonedDateTime endTime) {
        if (nonNull(startTime) && nonNull(endTime)) {
            return storeSlotRepository
                .findAllSlotsBetweenStartTimeAndEndTime(startTime, endTime)
                .stream().findFirst()
                .orElseThrow(() -> slotNotFoundException(startTime, endTime));
        }
        throw slotNotFoundException(startTime, endTime);
    }

    AppointmentException slotNotFoundException(ZonedDateTime startTime,
                                               ZonedDateTime endTime) {
        return new AppointmentException(
            String.format("""
                Unable to find appointment slot\s
                start time: '%s'
                end time  : '%s'""", startTime, endTime));
    }

    AppointmentException storeNotFoundException(String storeCode) {
        return new AppointmentException(
            String.format("Unable to find store " +
                "with code: '%s'", storeCode));
    }

    void validateInputs(StoreDto storeDto, StoreSlotDto slotDto) {
        if (isNull(storeDto) ||
            isNull(storeDto.getStoreCode()) ||
            isNull(slotDto) ||
            isNull(slotDto.getStartTime()) ||
            isNull(slotDto.getEndTime())) {
            throw new AppointmentException(String.format(
                """
                    Null value for find appointment,\s
                    store: '%s',\s
                    slot: '%s'""", storeDto, slotDto
            ));
        }
    }

    Store getStore(String storeCode) {
        if (isNull(storeCode)) {
            throw storeNotFoundException(null);
        }
        return storeService.findByStoreCode(storeCode)
            .stream().findFirst()
            .orElseThrow(() -> storeNotFoundException(storeCode));
    }
}
