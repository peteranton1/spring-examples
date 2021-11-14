package com.example.restreactive.controller;


import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
public class StoreController extends ControllerExceptionHandler {

    public static final String GET_STORES_LIMIT = "/stores/{limit}";
    public static final String GET_STORE_STORE_CODE = "/store/{storeCode}";
    public static final String PUT_STORE = "/store";
    public static final String DELETE_STORE = "/store/{storeCode}";

    @Autowired
    private StoreService storeService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(value = GET_STORES_LIMIT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<StoreDto> listAllStoresWithLimit(@PathVariable int limit) {
        final int max = 1000;
        int limitTemp = (limit < max ? limit : max);
        List<StoreDto> storeDtos = storeService.findAllStores();
        log.info("Controller: stores {} ", storeDtos.size());
        return Flux
            .fromStream(storeDtos.stream())
            .take(limitTemp)
            //.delayElements(Duration.ofMillis(100))
            ;
    }

    @GetMapping(value = GET_STORE_STORE_CODE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<StoreDto> findStore(@PathVariable String storeCode) {
        AppointmentException ap = new AppointmentException(
            "Store not found: " + storeCode,
            HttpStatus.NOT_FOUND);
        return Mono.just(
            storeService.findByStoreCode(storeCode)
                .stream()
                .map(store -> (StoreDto)modelMapper.toDto(store))
                .findFirst()
                .orElseThrow(() -> ap)
        );
    }

    @PutMapping(value = PUT_STORE,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<StoreDto> upsertStore(@RequestBody StoreDto request) {
        return Mono.just(
            storeService.upsertStore(request));
    }

    @DeleteMapping(value = DELETE_STORE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<MessageDto> deleteStore(@PathVariable String storeCode) {
        return Mono.just(
            storeService.deleteStore(storeCode));
    }

}
