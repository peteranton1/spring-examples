package com.example.restreactive.controller;

import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.mapping.StoreSlotMapper;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.repository.StoreSlotRepository;
import com.example.restreactive.service.StoreSlotService;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {StoreSlotController.class})
@Import({StoreSlotService.class,
    ModelMapper.class,
    StoreSlotMapper.class})
class StoreSlotControllerTest {

    private static final String SLOT_1 = "slot1";
    private static final String STORE_1 = "store1";
    private static final String START_TIME_STR_1 = "2019-04-01T16:24:11.252Z";
    private static final String END_TIME_STR_1 = "2019-04-01T16:54:11.252Z";

    private static final String STORE_SLOTS_START_TIME_LIMIT =
        "/store/slots/" + START_TIME_STR_1 + "/10";
    private static final String PUT_STORE_STORE_CODE_SLOT =
        "/store/" + STORE_1 + "/slot";
    private static final String DELETE_STORE_STORE_CODE_SLOT_SLOT_CODE =
        "/store/" + STORE_1 + "/slot/" + SLOT_1;

    private static final ZonedDateTime START_TIME_1 = ZonedDateTime
        .parse(START_TIME_STR_1).withZoneSameLocal(ZoneId.of("UTC"));

    private static final ZonedDateTime END_TIME_1 = ZonedDateTime
        .parse(END_TIME_STR_1).withZoneSameLocal(ZoneId.of("UTC"));


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private StoreSlotRepository storeSlotRepository;

    @Test
    void whenListAllSlotsForStartDateWithLimitAndEmptyThenEmpty() {
        when(storeSlotRepository.findAllSlotsBetweenStartTimeAndEndTime(
            START_TIME_1, END_TIME_1))
            .thenReturn(emptyList());

        List<?> actual = webTestClient.get()
            .uri(STORE_SLOTS_START_TIME_LIMIT)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = emptyList();
        assertEquals(expected, actual);
    }

    @Test
    void whenListAllSlotsForStartDateWithLimitAnd1RecThen1Rec() {
        StoreSlotDto storeSlotDto = getStoreSlotDto(1);
        StoreSlot storeSlot = (StoreSlot) modelMapper.toEntity(storeSlotDto);
        when(storeSlotRepository.findAllSlotsBetweenStartTimeAndEndTime(
            any(), any()))
            .thenReturn(ImmutableList.of(storeSlot));

        List<?> actual = webTestClient.get()
            .uri(STORE_SLOTS_START_TIME_LIMIT)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = ImmutableList.of(asMapStoreDto(storeSlotDto));
        assertEquals(expected, actual);
    }

    @Test
    void whenListAllSlotsForStoresAndStartDateWithLimitEmptyThenEmpty() {
        when(storeSlotRepository.findAllSlotsByStoresListBetweenStartTimeAndEndTime(
            any(), any(), any()))
            .thenReturn(emptyList());

        List<?> actual = webTestClient.post()
            .uri(STORE_SLOTS_START_TIME_LIMIT)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(ImmutableList.of(STORE_1)), List.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = emptyList();
        assertEquals(expected, actual);
    }

    @Test
    void whenListAllSlotsForStoresAndStartDateWithLimitAnd1RecThen1Rec() {
        StoreSlotDto storeSlotDto = getStoreSlotDto(1);
        StoreSlot storeSlot = (StoreSlot) modelMapper.toEntity(storeSlotDto);
        when(storeSlotRepository.findAllSlotsByStoresListBetweenStartTimeAndEndTime(
            any(), any(), any()))
            .thenReturn(ImmutableList.of(storeSlot));

        List<?> actual = webTestClient.post()
            .uri(STORE_SLOTS_START_TIME_LIMIT)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(ImmutableList.of(STORE_1)), List.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = ImmutableList.of(asMapStoreDto(storeSlotDto));
        assertEquals(expected, actual);
    }

    @Test
    void whenUpsertStoreInsertThenOk() {
        StoreSlotDto storeSlotDto = getStoreSlotDto(1);
        StoreSlot storeSlot = (StoreSlot) modelMapper.toEntity(storeSlotDto);
        when(storeSlotRepository.findAllSlotsByStoreBetweenStartTimeAndEndTime(
            START_TIME_1, END_TIME_1, STORE_1))
            .thenReturn(ImmutableList.of(storeSlot));
        when(storeSlotRepository.save(any()))
            .thenReturn(storeSlot);

        StoreSlotDto actual = webTestClient.put()
            .uri(PUT_STORE_STORE_CODE_SLOT)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(storeSlotDto), StoreDto.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(StoreSlotDto.class)
            .returnResult().getResponseBody();

        //StoreSlotDto expected = asMapStoreDto(storeSlotDto);
        assertEquals(storeSlotDto, actual);
    }

    @Test
    void whenDeleteStoreExistsThenOk() {
        MessageDto messageDto = MessageDto.builder()
            .code("200")
            .message("Store Slot deleted: " + STORE_1 + "/" + SLOT_1)
            .build();
        StoreSlotDto storeSlotDto = getStoreSlotDto(1);
        StoreSlot storeSlot = (StoreSlot) modelMapper.toEntity(storeSlotDto);
        when(storeSlotRepository.findAllSlotsByStoreBetweenStartTimeAndEndTime(
            START_TIME_1, END_TIME_1, STORE_1))
            .thenReturn(ImmutableList.of(storeSlot));

        MessageDto actual = webTestClient.delete()
            .uri(DELETE_STORE_STORE_CODE_SLOT_SLOT_CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        assertEquals(messageDto, actual);
    }

    private StoreSlotDto getStoreSlotDto(int i) {
        String slotCode = "slot" + i;
        String storeCode = "store" + i;
        return StoreSlotDto.builder()
            .id(i)
            .slotCode(slotCode)
            .storeCode(storeCode)
            .startTime(START_TIME_1)
            .endTime(END_TIME_1)
            .build();
    }

    private Map<String, Object> asMapStoreDto(StoreSlotDto storeSlotDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (storeSlotDto != null) {
            outerMap.put("id", storeSlotDto.getId());
            outerMap.put("slotCode", storeSlotDto.getSlotCode());
            outerMap.put("storeCode", storeSlotDto.getStoreCode());
            outerMap.put("startTime", START_TIME_STR_1);
            outerMap.put("endTime", END_TIME_STR_1);
        }
        return outerMap;
    }

}