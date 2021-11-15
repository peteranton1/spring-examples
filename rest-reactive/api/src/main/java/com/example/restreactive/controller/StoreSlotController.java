package com.example.restreactive.controller;


import com.example.restreactive.controller.ControllerExceptionHandler;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.service.StoreSlotService;
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
public class StoreSlotController extends ControllerExceptionHandler {

    public static final String STORE_SLOTS_START_TIME_LIMIT = "/stores/slots/{startTime}/{limit}";
    public static final String PUT_STORE_STORE_CODE_SLOT = "/store/{storeCode}/slot";
    public static final String DELETE_STORE_STORE_CODE_SLOT_SLOT_CODE = "/store/{storeCode}/slot/{slotCode}";
    public static final long PLUS_DAYS = 7L;
    public static final int MAX_RECORDS = 1000;

    @Autowired
    private StoreSlotService storeSlotService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = STORE_SLOTS_START_TIME_LIMIT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<StoreSlotDto> listAllSlotsForStartDateWithLimit(
        @PathVariable String startTime,
        @PathVariable int limit
    ) {
        ZonedDateTime[] startTimes = getZonedDateTimes(startTime);
        List<StoreSlotDto> storeSlotDtos = storeSlotService
            .findByStartTimeAndEndTime(startTimes[0], startTimes[1]);
        return getDtoFlux(limit, storeSlotDtos);
    }

    @PostMapping(value = STORE_SLOTS_START_TIME_LIMIT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Flux<StoreSlotDto> listAllSlotsForStoresAndStartDateWithLimit(
        @PathVariable String startTime,
        @PathVariable int limit,
        @RequestBody List<String> storeCodes
    ) {
        ZonedDateTime[] startTimes = getZonedDateTimes(startTime);
        List<StoreSlotDto> storeSlotDtos = storeSlotService
            .findByStoreCodeListAndStartTimeAndEndTime(
                storeCodes, startTimes[0], startTimes[1]
            );
        return getDtoFlux(limit, storeSlotDtos);
    }

    @PutMapping(value = PUT_STORE_STORE_CODE_SLOT,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<StoreSlotDto> upsertStore(@RequestBody StoreSlotDto request) {
        return Mono.just(
            storeSlotService.upsertAppointmentSlot(request));
    }

    @DeleteMapping(value = DELETE_STORE_STORE_CODE_SLOT_SLOT_CODE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<MessageDto> deleteStore(
        @PathVariable String storeCode,
        @PathVariable String slotCode
    ) {
        return Mono.just(
            storeSlotService.deleteAppointmentSlot(storeCode, slotCode));
    }

    private Flux<StoreSlotDto> getDtoFlux(
        int limit,
        List<StoreSlotDto> slotDtos
    ) {
        log.info("Controller: slots {} ", slotDtos.size());
        return Flux
            .fromStream(slotDtos.stream())
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
