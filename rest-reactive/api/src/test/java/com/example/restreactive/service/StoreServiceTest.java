package com.example.restreactive.service;

import com.example.restreactive.dto.CountryDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StreetAddressDto;
import com.example.restreactive.model.Store;
import com.example.restreactive.repository.CountryRepository;
import com.example.restreactive.repository.StoreRepository;
import com.example.restreactive.repository.StreetAddressRepository;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private EntityDtoCreator creator = new EntityDtoCreator();

    @BeforeEach
    void setUp() {
        deleteAll();
    }

    @AfterEach
    void tearDown() {
        deleteAll();
    }

    private void deleteAll() {
        storeRepository.deleteAll();
    }

    @Test
    void whenFindAllStores() {
        assertStoresSize(0);
    }

    @Test
    void whenFindByStoreCodeEmptyThenEmpty() {
        List<Store> expected = ImmutableList.of();
        List<Store> actual = underTest.findByStoreCode("non-existent");
        assertEquals(expected, actual);
    }

    @Test
    void whenUpsertStoreCreateThenCreate() {

        // Check 0 users in db, initial conditions
        assertStoresSize(0);

        // Step 1 - save
        CountryDto countryDto = creator.createCountryDto();
        StreetAddressDto streetAddressDto = creator.createStreetAddressDto(countryDto);
        StoreDto storeDto = creator.createStoreDto(streetAddressDto);
        StoreDto expected = creator.createStoreDto(streetAddressDto);
        StoreDto actual = underTest.upsertStore(storeDto);
        // we don't know the id that will be created so set it to
        // whatever it was so following assertEquals works
        expected.setId(actual.getId());
        expected.setStoreCode(actual.getStoreCode());
        assertEquals(expected, actual);

        // Check 1 in db, therefore created
        assertStoresSize(1);
    }

    @Test
    void whenUpsertStoreUpdateThenUpdate() {

        // Check 0 users in db, initial conditions
        assertStoresSize(0);

        // Step 1 - save
        CountryDto countryDto = creator.createCountryDto();
        StreetAddressDto streetAddressDto = creator.createStreetAddressDto(countryDto);
        StoreDto storeDto = creator.createStoreDto(streetAddressDto);
        StoreDto expected = creator.createStoreDto(streetAddressDto);
        StoreDto actual = underTest.upsertStore(storeDto);
        // we don't know the id that will be created so set it to
        // whatever it was so following assertEquals works
        expected.setId(actual.getId());
        expected.setStoreCode(actual.getStoreCode());
        assertEquals(expected, actual);

        // Check 1 in db, therefore created
        assertStoresSize(1);

        // Step 2 - update
        StoreDto actual2 = underTest.upsertStore(storeDto);
        assertEquals(expected, actual2);

        // Check 1 in db, therefore updated
        assertStoresSize(1);
    }

    @Test
    void whenDeleteStoreExistsThenDelete() {

        // Check 0 users in db, initial conditions
        assertStoresSize(0);

        // Step 1 - save
        CountryDto countryDto = creator.createCountryDto();
        StreetAddressDto streetAddressDto = creator.createStreetAddressDto(countryDto);
        StoreDto storeDto = creator.createStoreDto(streetAddressDto);
        StoreDto expected = creator.createStoreDto(streetAddressDto);
        StoreDto actual = underTest.upsertStore(storeDto);
        // we don't know the id that will be created so set it to
        // whatever it was so following assertEquals works
        expected.setId(actual.getId());
        expected.setStoreCode(actual.getStoreCode());
        assertEquals(expected, actual);

        // Check 1 in db, therefore created
        assertStoresSize(1);

        // Step 2 - delete
        String storeCode = storeDto.getStoreCode();
        MessageDto expected2 = MessageDto.builder()
            .code("200")
            .message("Store deleted: " + expected.getStoreCode())
            .build();
        MessageDto actual2 = underTest.deleteStore(storeCode);
        assertEquals(expected2, actual2);

        // Check 1 in db, therefore updated
        assertStoresSize(0);
    }

    private void assertStoresSize(int expectedSize) {
        List<StoreDto> stores = underTest.findAllStores();
        assertEquals(expectedSize, stores.size());
    }
}
