package com.example.restreactive.service;

import com.example.restreactive.dto.AppointmentSlotDto;
import com.example.restreactive.repository.AppointmentSlotRepository;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentSlotServiceTest {

    public static final ZonedDateTime DATE_TIME_1 = ZonedDateTime
        .parse("2019-04-01T16:24:11.252Z");

    public static final ZonedDateTime DATE_TIME_2 = DATE_TIME_1
        .plusMinutes(30);

    @Autowired
    private AppointmentSlotService underTest;

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @BeforeEach
    void setUp() {
        appointmentSlotRepository.deleteAll();
    }

    @Test
    void whenFindAllSlotsSizeThenEmpty() {
        assertSlotsSize(0);
    }

    @Test
    void whenFindByStartTimeAndEndTimeEmptyThenEmpty() {
        List<AppointmentSlotDto> expected = ImmutableList.of();
        List<AppointmentSlotDto> actual = underTest.findByStartTimeAndEndTime(
            DATE_TIME_1, DATE_TIME_2);
        assertEquals(expected, actual);
    }

    @Test
    void whenUpsertAppointmentSlotCreateThenCreate() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Step 1 - save
        AppointmentSlotDto appointmentSlotDto = createAppointmentSlotDto(
        );
        long actual = underTest.upsertAppointmentSlot(appointmentSlotDto);
        long expected = 1L;
        assertTrue(expected <= actual);

        // Check 1 in db, therefore created
        assertSlotsSize(1);
    }

    @Test
    void whenUpsertAppointmentSlotExistsThenUpdate() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Step 1 - save
        AppointmentSlotDto appointmentSlotDto = createAppointmentSlotDto(
        );
        long actual1 = underTest.upsertAppointmentSlot(appointmentSlotDto);
        long expected = 1L;
        assertTrue(expected <= actual1);

        // Check 1 in db, therefore created
        assertSlotsSize(1);

        // Step 2 - Update
        long actual2 = underTest.upsertAppointmentSlot(appointmentSlotDto);
        assertEquals(actual1, actual2);

        // Check 1 in db, therefore updated
        assertSlotsSize(1);
    }

    private void assertSlotsSize(int expectedSize) {
        List<AppointmentSlotDto> slots = underTest.findAllSlots();
        assertEquals(expectedSize, slots.size());
    }

    private AppointmentSlotDto createAppointmentSlotDto() {
        return AppointmentSlotDto.builder()
            .startTime(DATE_TIME_1)
            .endTime(DATE_TIME_2)
            .build();
    }
}