package com.example.restreactive.controller;


import com.example.restreactive.dto.AppointmentDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@RestController
public class AppointmentController extends ControllerExceptionHandler {

    public static final String APPOINTMENT_START_TIME_LIMIT = "/appointments/{startTime}/{limit}";
    public static final String PUT_APPOINTMENT = "/appointment";
    public static final String DELETE_APPOINTMENT = "/appointment/{appointmentCode}";
    public static final long PLUS_DAYS = 7L;
    public static final int MAX_RECORDS = 1000;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = APPOINTMENT_START_TIME_LIMIT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<AppointmentDto> listAllAppointmentsForStartDateWithLimit(
        @PathVariable String startTime,
        @PathVariable int limit
    ) {
        ZonedDateTime[] startTimes = getZonedDateTimes(startTime);
        List<AppointmentDto> appointmentDtos = appointmentService
            .findByStartTimeAndEndTime(startTimes[0], startTimes[1]);
        return getDtoFlux(limit, appointmentDtos);
    }

    @PostMapping(value = APPOINTMENT_START_TIME_LIMIT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Flux<AppointmentDto> listAllAppointmentsForStoresAndStartDateWithLimit(
        @PathVariable String startTime,
        @PathVariable int limit,
        @RequestBody List<String> storeCodes
    ) {
        ZonedDateTime[] startTimes = getZonedDateTimes(startTime);
        List<AppointmentDto> appointmentDtos = appointmentService
            .findByStartTimeAndEndTimeAndStoreCodeList(
                startTimes[0], startTimes[1], storeCodes
            );
        return getDtoFlux(limit, appointmentDtos);
    }

    @PutMapping(value = PUT_APPOINTMENT,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<AppointmentDto> upsertAppointment(@RequestBody AppointmentDto request) {
        return Mono.just(
            appointmentService.upsertAppointment(request));
    }

    @DeleteMapping(value = DELETE_APPOINTMENT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<MessageDto> deleteStore(
        @PathVariable String appointmentCode
    ) {
        return Mono.just(
            appointmentService.deleteAppointment(appointmentCode));
    }

    private Flux<AppointmentDto> getDtoFlux(
        int limit,
        List<AppointmentDto> appointmentDtos
    ) {
        log.info("Controller: slots {} ", appointmentDtos.size());
        return Flux
            .fromStream(appointmentDtos.stream())
            .take(effectiveLimit(limit))
            //.delayElements(Duration.ofMillis(100))
            ;
    }

    private int effectiveLimit(int limit) {
        return Math.min(limit, MAX_RECORDS);
    }

    private ZonedDateTime[] getZonedDateTimes(String startTime) {
        ZonedDateTime startTimeDT = getZonedDateTime(startTime);
        return new ZonedDateTime[]{
            startTimeDT,
            startTimeDT.plusDays(PLUS_DAYS)
        };
    }

    private ZonedDateTime getZonedDateTime(String startTime) {
        try {
            return ZonedDateTime.parse(startTime);
        } catch (Exception e) {
            throw new AppointmentException(String.format(
                "Unable to parse date: %s", startTime), e);
        }
    }
}
