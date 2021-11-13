package com.example.restreactive.controller;


import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreDto;
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

    @Autowired
    private StoreSlotService storeSlotService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = "/store/{storeCode}/slots/{startTime}/{limit}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<StoreSlotDto> listAllSlotsWithLimit(
        @PathVariable String storeCode,
        @PathVariable String startTime,
        @PathVariable int limit
        ) {
        final int max = 1000;
        ZonedDateTime startTimeDT = getZonedDateTime(startTime);
        int limitTemp = (limit < max ? limit : max);
        List<StoreSlotDto> slotDtos = storeSlotService.findAllSlots();
        log.info("Controller: slots {} ", slotDtos.size());
        return Flux
            .fromStream(slotDtos.stream())
            .take(limitTemp)
            //.delayElements(Duration.ofMillis(100))
            ;
    }

    private ZonedDateTime getZonedDateTime(String startTime) {
        try {
            return ZonedDateTime
                .parse(startTime);
        } catch(Exception e){
            throw new AppointmentException(String.format(
                "Unable to parse date: %s", startTime), e);
        }
    }

    @GetMapping(value = "/store/{storeCode}/slot/{startTime}/{endTime}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<StoreSlotDto> findAppointmentSlot(
        @PathVariable String storeCode,
        @PathVariable String startTime,
        @PathVariable String endTime
        ) {
        AppointmentException ap = new AppointmentException(
            "Slot not found: " + startTime + ", " + endTime,
            HttpStatus.NOT_FOUND);
        ZonedDateTime startTimeDT = getZonedDateTime(startTime);
        ZonedDateTime endTimeDT = getZonedDateTime(endTime);
        return Mono.just(
            storeSlotService.findByStoreCodeAndStartTimeAndEndTime(
                storeCode, startTimeDT, endTimeDT)
                .stream()
                .findFirst()
                .orElseThrow(() -> ap)
        );
    }

    @PutMapping(value = "/store/{storeCode}/slot",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<StoreSlotDto> upsertStore(@RequestBody StoreSlotDto request) {
        return Mono.just(
            storeSlotService.upsertAppointmentSlot(request));
    }

    @DeleteMapping(value = "/store/{slotCode}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<MessageDto> deleteStore(@PathVariable String storeCode) {
        return Mono.just(
            storeSlotService.deleteAppointmentSlot(storeCode));
    }

}
