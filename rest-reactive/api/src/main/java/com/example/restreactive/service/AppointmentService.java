package com.example.restreactive.service;

import com.example.restreactive.dto.*;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.mapping.ZonedDateTimeHelper;
import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.AppointmentRepository;
import com.example.restreactive.repository.StoreSlotRepository;
import com.example.restreactive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
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

    private final ServiceHelper serviceHelper = new ServiceHelper();

    private final ZonedDateTimeHelper zonedDateTimeHelper = new ZonedDateTimeHelper();

    public List<AppointmentDto> findAllAppointments() {
        return appointmentRepository.findAll()
            .stream()
            .map(appointment -> (AppointmentDto) modelMapper.toDto(appointment))
            .toList();
    }

    public List<AppointmentDto> findByStartTimeAndEndTime(ZonedDateTime startTime,
                                                          ZonedDateTime endTime) {
        if (isNull(startTime) || isNull(endTime) ) {
            return emptyList();
        }
        List<Appointment> appointments = appointmentRepository
            .findAllAppointmentsBetweenStartTimeAndEndTime(startTime, endTime);
        return appointments
            .stream()
            .map(slot -> (AppointmentDto) modelMapper.toDto(slot))
            .toList();
    }

    public List<AppointmentDto> findByStartTimeAndEndTimeAndStoreCodeList(
        ZonedDateTime startTime,
        ZonedDateTime endTime,
        List<String> storeCodes
    ) {

        if (isNull(startTime) || isNull(endTime) ||
            isNull(storeCodes) || storeCodes.isEmpty()) {
            return emptyList();
        }
        List<String> storeCodes1 = storeCodes.stream()
            .map(String::toLowerCase).toList();
        ZonedDateTime startTime1 = zonedDateTimeHelper.ensureZone(startTime);
        ZonedDateTime endTime1 = zonedDateTimeHelper.ensureZone(endTime);
        List<Appointment> appointments = appointmentRepository
            .findAllAppointmentsByStoresListBetweenStartTimeAndEndTime(
                startTime1, endTime1, storeCodes1);
        return appointments
            .stream()
            .map(slot -> (AppointmentDto) modelMapper.toDto(slot))
            .toList();
    }

    public List<AppointmentDto> findByStoreAndAppointmentSlot(
        StoreDto storeDto, StoreSlotDto slotDto) {

        // Validate Input
        validateInputs(storeDto, slotDto, List.of());

        // Get Entities
        StoreSlot slot = getStoreSlot(
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

    public AppointmentDto upsertAppointment(AppointmentDto appointmentDto) {
        // Validate Input
        verifyAppointmentDtoForUpdate(appointmentDto);

        StoreDto storeDto = appointmentDto.getStore();
        StoreSlotDto slotDto = appointmentDto.getStoreSlot();
        List<UserDto> userDtos = appointmentDto.getUsers();
        validateInputs(storeDto, slotDto, userDtos);

        // Get Entities and ids
        StoreSlot slot = getStoreSlot(
            slotDto.getStartTime(), slotDto.getEndTime());
        appointmentDto.getStoreSlot().setId(slot.getId());

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
        Appointment appointmentOut = appointmentRepository.save(appointmentRepository
            .findByStoreAndStoreSlot(
                store,
                slot
            ).stream().findAny()
            .map(appointment -> (Appointment) modelMapper
                .update(appointment, appointmentDto))
            .orElse((Appointment) modelMapper.insert(appointmentDto))
        );
        serviceHelper.assertNonNull("appointmentOut", appointmentOut);
        return (AppointmentDto) modelMapper.toDto(appointmentOut);
    }

    private void verifyAppointmentDtoForUpdate(AppointmentDto appointmentDto) {
        serviceHelper.assertNonNull("appointmentDto", appointmentDto);
        if (isNull(appointmentDto.getAppointmentCode())) {
            appointmentDto.setAppointmentCode(UUID.randomUUID().toString());
        }
        appointmentDto.setAppointmentCode(appointmentDto
            .getAppointmentCode().toLowerCase());
    }


    public MessageDto deleteAppointment(
        String appointmentCode
    ) {
        if (isNull(appointmentCode)) {
            return MessageDto.builder()
                .code("200")
                .message(String.format("Appointment not specified. %s", appointmentCode))
                .build();
        }
        appointmentCode = appointmentCode.toLowerCase();

        return appointmentRepository
            .findByAppointmentCode(appointmentCode)
            .stream().findFirst()
            .map(appt -> {
                appointmentRepository.delete(appt);
                return MessageDto.builder()
                    .code("200")
                    .message("Appointment deleted: " + appt.getAppointmentCode())
                    .build();
            })
            .orElse( MessageDto.builder()
                .code("200")
                .message("Appointment not found: " + appointmentCode)
                .build());
    }

    StoreSlot getStoreSlot(ZonedDateTime startTime,
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

    void validateInputs(StoreDto storeDto, StoreSlotDto slotDto, List<UserDto> userDtos) {
        if (isNull(storeDto) ||
            isNull(storeDto.getStoreCode()) ||
            isNull(slotDto) ||
            isNull(slotDto.getStartTime()) ||
            isNull(slotDto.getEndTime()) ||
            isNull(userDtos)
        ) {
            throw new AppointmentException(String.format(
                """
                    Null value for find appointment,\s
                    store: '%s',\s
                    slot: '%s'\s
                    users: '%s'""", storeDto, slotDto, userDtos
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
