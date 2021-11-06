package com.example.restreactive.service;

import com.example.restreactive.dto.*;
import com.example.restreactive.model.Country;
import com.example.restreactive.repository.CountryRepository;
import com.example.restreactive.repository.StoreRepository;
import com.example.restreactive.repository.StreetAddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreServiceTest {

    @Autowired
    private StoreService underTest;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StreetAddressRepository streetAddressRepository;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void setUp() {
        storeRepository.deleteAll();
    }

    @Test
    void whenFindAllStores() {
        assertStoresSize(0);
    }

    @Test
    void whenFindByStoreCodeEmptyThenEmpty() {
        Optional<StoreDto> expected = Optional.empty();
        Optional<StoreDto> actual = underTest.findByStoreCode("non-existent");
        assertEquals(expected, actual);
    }

    @Test
    void whenUpsertStoreCreateThenCreate() {

        // Check 0 users in db, initial conditions
        assertStoresSize(0);

        // Step 1 - save
        CountryDto countryDto = createCountryDto();
        StreetAddressDto streetAddressDto = createStreetAddressDto(countryDto);
        StoreDto storeDto = createStoreDto(streetAddressDto);
        StoreDto expected = createStoreDto(streetAddressDto);
        StoreDto actual = underTest.upsertStore(storeDto);
        // we don't know the id that will be created so set it to
        // whatever it was so following assertEquals works
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        // Check 1 in db, therefore created
        assertStoresSize(1);
    }

    @Test
    void whenUpsertStoreUpdateThenUpdate() {

        // Check 0 users in db, initial conditions
        assertStoresSize(0);

        // Step 1 - save
        CountryDto countryDto = createCountryDto();
        StreetAddressDto streetAddressDto = createStreetAddressDto(countryDto);
        StoreDto storeDto = createStoreDto(streetAddressDto);
        StoreDto expected = createStoreDto(streetAddressDto);
        StoreDto actual = underTest.upsertStore(storeDto);
        // we don't know the id that will be created so set it to
        // whatever it was so following assertEquals works
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        // Check 1 in db, therefore created
        assertStoresSize(1);

        // Step 2 - update
        StoreDto actual2 = underTest.upsertStore(storeDto);
        assertEquals(expected, actual2);

        // Check 1 in db, therefore updated
        assertStoresSize(1);
    }

    private void assertStoresSize(int expectedSize) {
        List<StoreDto> stores = underTest.findAllStores();
        assertEquals(expectedSize, stores.size());
    }

    private StoreDto createStoreDto(StreetAddressDto streetAddressDto) {
        return StoreDto.builder()
            .storeName("mystore")
            .storeCode("mycode")
            .address(streetAddressDto)
            .build();
    }

    private StreetAddressDto createStreetAddressDto(CountryDto countryDto) {
        return StreetAddressDto.builder()
            .line1("line1")
            .line2("line2")
            .city("city")
            .county("county")
            .country(countryDto)
            .postcode("postcode")
            .build();
    }

    private CountryDto createCountryDto() {
        return CountryDto.builder()
            .name("name")
            .code("code")
            .build();
    }

}