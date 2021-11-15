package com.example.restreactive.controller;

import com.example.restreactive.controller.StoreController;
import com.example.restreactive.dto.CountryDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StreetAddressDto;
import com.example.restreactive.mapping.CountryMapper;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.mapping.StoreMapper;
import com.example.restreactive.mapping.StreetAddressMapper;
import com.example.restreactive.model.Store;
import com.example.restreactive.repository.CountryRepository;
import com.example.restreactive.repository.StoreRepository;
import com.example.restreactive.repository.StreetAddressRepository;
import com.example.restreactive.service.StoreService;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {StoreController.class})
@Import({StoreService.class,
    ModelMapper.class,
    StoreMapper.class,
    StreetAddressMapper.class,
    CountryMapper.class})
class StoreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private StoreRepository storeRepository;

    @MockBean
    private StreetAddressRepository streetAddressRepository;

    @MockBean
    private CountryRepository countryRepository;

    @Test
    void whenListAllStoresWithLimit10AndEmptyThenEmpty() {
        when(storeRepository.findAll())
            .thenReturn(emptyList());

        List<?> actual = webTestClient.get()
            .uri("/stores/10")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = emptyList();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenListAllStoresWithLimit10And1RecThen1Rec() {
        StoreDto storeDto = getStoreDto(1);
        Store store = (Store)modelMapper.toEntity(storeDto);
        when(storeRepository.findAll())
            .thenReturn(ImmutableList.of(store));

        List<?> actual = webTestClient.get()
            .uri("/stores/10")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = ImmutableList.of(asMapStoreDto(storeDto));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenFindStoreNotExistsThenNotFound() {
        when(storeRepository.findByStoreCode("store1"))
            .thenReturn(emptyList());

        MessageDto actual = webTestClient.get()
            .uri("/store/store1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        MessageDto expected = MessageDto.builder()
            .code("404")
            .message("AppointmentException: Store not found: store1")
            .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenFindStoreExistsThenOk() {
        StoreDto storeDto = getStoreDto(1);
        Store store = (Store)modelMapper.toEntity(storeDto);
        when(storeRepository.findByStoreCode("store1"))
            .thenReturn(ImmutableList.of(store));

        StoreDto actual = webTestClient.get()
            .uri("/store/store1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(StoreDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(storeDto, actual);
    }

    @Test
    void whenUpsertStoreInsertThenOk() {
        StoreDto storeDto = getStoreDto(1);
        Store store = (Store)modelMapper.toEntity(storeDto);
        when(storeRepository.findByStoreCode(any()))
            .thenReturn(ImmutableList.of(store));
        when(storeRepository.save(any()))
            .thenReturn(store);
        when(streetAddressRepository.save(any()))
            .thenReturn(store.getAddress());
        when(countryRepository.save(any()))
            .thenReturn(store.getAddress().getCountry());

        StoreDto actual = webTestClient.put()
            .uri("/store")
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(storeDto),StoreDto.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(StoreDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(storeDto, actual);
    }

    @Test
    void whenDeleteStoreExistsThenOk() {
        String storeCode = "store1";
        MessageDto messageDto = MessageDto.builder()
            .code("200")
            .message("Store deleted: " + storeCode)
            .build();
        StoreDto storeDto = getStoreDto(1);
        Store store = (Store)modelMapper.toEntity(storeDto);
        when(storeRepository.findByStoreCode("store1"))
            .thenReturn(ImmutableList.of(store));

        MessageDto actual = webTestClient.delete()
            .uri("/store/store1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(messageDto, actual);
    }

    private StoreDto getStoreDto(int i) {
        String storeCode = "store" + i;
        return StoreDto.builder()
            .id(i)
            .storeCode(storeCode)
            .storeName(storeCode)
            .address(getStreetAddressDto(i))
            .build();
    }

    private StreetAddressDto getStreetAddressDto(int i) {
        String line = "line" + i;
        return StreetAddressDto.builder()
            .id(i)
            .line1(line)
            .line2(line)
            .city(line)
            .county(line)
            .country(getCountryDto(i))
            .postcode(line)
            .build();
    }

    private CountryDto getCountryDto(int i) {
        String name = "country" + i;
        return CountryDto.builder()
            .id(i)
            .name(name)
            .code(name)
            .build();
    }

    private Map<String, Object> asMapStoreDto(StoreDto storeDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (storeDto != null) {
            outerMap.put("id", storeDto.getId());
            outerMap.put("storeName", storeDto.getStoreName());
            outerMap.put("storeCode", storeDto.getStoreCode());
            outerMap.put("address", asMapAddressDto(storeDto.getAddress()));
        }
        return outerMap;
    }

    private Map<String, Object> asMapAddressDto(StreetAddressDto addressDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (addressDto != null) {
            outerMap.put("id", addressDto.getId());
            outerMap.put("line1", addressDto.getLine1());
            outerMap.put("line2", addressDto.getLine2());
            outerMap.put("city", addressDto.getCity());
            outerMap.put("county", addressDto.getCounty());
            outerMap.put("country", asMapCountryDto(addressDto.getCountry()));
            outerMap.put("postcode", addressDto.getPostcode());
        }
        return outerMap;
    }

    private Map<String, Object> asMapCountryDto(CountryDto countryDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (countryDto != null) {
            outerMap.put("id", countryDto.getId());
            outerMap.put("name", countryDto.getName());
            outerMap.put("code", countryDto.getCode());
        }
        return outerMap;
    }
}