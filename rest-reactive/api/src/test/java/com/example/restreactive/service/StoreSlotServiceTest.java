package com.example.restreactive.service;

import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.repository.AppointmentRepository;
import com.example.restreactive.repository.StoreSlotRepository;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class StoreSlotServiceTest {

    public static final String TEST = "test";

    public static final ZonedDateTime DATE_TIME_1 = ZonedDateTime
        .parse("2019-04-01T16:24:11.252Z");

    public static final ZonedDateTime DATE_TIME_2 = DATE_TIME_1
        .plusMinutes(30);

    @Autowired
    private StoreSlotService underTest;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StoreSlotRepository storeSlotRepository;

    private final EntityDtoCreator creator = new EntityDtoCreator();

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();
        storeSlotRepository.deleteAll();
    }

    @Test
    void whenFindAllSlotsSizeThenEmpty() {
        assertSlotsSize(0);
    }

    @Test
    void whenFindByStoreAndStartTimeAndEndTimeEmptyThenEmpty() {
        List<StoreSlotDto> expected = ImmutableList.of();
        List<StoreSlotDto> actual = underTest.findByStoreCodeAndStartTimeAndEndTime(
            "non-existent",
            DATE_TIME_1, DATE_TIME_2);
        assertEquals(expected, actual);
    }

    @Test
    void whenFindByStoreCodeListAndStartTimeAndEndTimeEmptyThenEmpty() {
        List<StoreSlotDto> expected = ImmutableList.of();
        List<StoreSlotDto> actual = underTest.findByStoreCodeListAndStartTimeAndEndTime(
            ImmutableList.of("non-existent"),
            DATE_TIME_1, DATE_TIME_2);
        assertEquals(expected, actual);
    }

    @Test
    void whenFindByStartTimeAndEndTimeEmptyThenEmpty() {
        List<StoreSlotDto> expected = ImmutableList.of();
        List<StoreSlotDto> actual = underTest.findByStartTimeAndEndTime(
            DATE_TIME_1, DATE_TIME_2);
        assertEquals(expected, actual);
    }

    @Test
    void whenUpsertAppointmentSlotCreateThenCreate() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Step 1 - save
        StoreSlotDto storeSlotDto = creator.createStoreSlotDto(
            TEST, TEST, DATE_TIME_1, DATE_TIME_2);
        StoreSlotDto actual = underTest.upsertAppointmentSlot(storeSlotDto);
        assertNotNull(actual);

        // Check 1 in db, therefore created
        assertSlotsSize(1);
    }

    @Test
    void whenUpsertAppointmentSlotExistsThenUpdate() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Step 1 - save
        StoreSlotDto appointmentSlotDto = creator.createStoreSlotDto(
            TEST, TEST, DATE_TIME_1, DATE_TIME_2);
        StoreSlotDto actual1 = underTest.upsertAppointmentSlot(appointmentSlotDto);
        assertNotNull(actual1);

        // Check 1 in db, therefore created
        assertSlotsSize(1);

        // Step 2 - Update
        StoreSlotDto actual2 = underTest.upsertAppointmentSlot(appointmentSlotDto);
        assertEquals(actual1, actual2);

        // Check 1 in db, therefore updated
        assertSlotsSize(1);
    }

    private void assertSlotsSize(int expectedSize) {
        List<StoreSlotDto> slots = underTest.findAllSlots();
        assertEquals(expectedSize, slots.size());
    }

}