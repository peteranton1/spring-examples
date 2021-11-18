package com.example.restreactive.service;

import com.example.restreactive.dto.AppointmentDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.model.Appointment;
import com.example.restreactive.repository.AppointmentRepository;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentServiceTest {

    public static final ZonedDateTime DATE_TIME_1 = ZonedDateTime
        .parse("2019-04-01T16:24:11.252Z");
    public static final ZonedDateTime DATE_TIME_2 = DATE_TIME_1
        .plusMinutes(30);
    public static final String EMAIL = "a@a.a";
    public static final String TEST = "test";

    @Autowired
    private AppointmentService underTest;

    @Autowired
    private StoreSlotService storeSlotService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private final EntityDtoCreator creator = new EntityDtoCreator();

    private StoreSlotDto storeSlotDto;

    private StoreDto storeDto;

    private UserDto userDto;


    @BeforeEach
    void setUp() {
        deleteAll();

        // Store slot
        storeSlotDto = creator
            .createStoreSlotDto(TEST, TEST, DATE_TIME_1, DATE_TIME_2);
        storeSlotService.upsertStoreSlot(storeSlotDto);

        // STORE
        storeDto = creator.createStoreDto(
            creator.createStreetAddressDto(
                creator.createCountryDto()));
        storeService.upsertStore(storeDto);

        // USER
        userDto = creator.createUserDto(
            creator.createEmailAddressDto(EMAIL));
        userService.upsertUser(userDto);
    }

    @AfterEach
    void tearDown() {
        deleteAll();
    }

    private void deleteAll() {
        appointmentRepository.deleteAll();
    }

    @Test
    void whenFindAllSizeThenEmpty() {
        assertSlotsSize(0);
    }

    @Test
    void whenFindByStartTimeAndEndTimeEmptyThenEmpty() {
        List<AppointmentDto> expected = ImmutableList.of();
        List<AppointmentDto> actual = underTest.findByStartTimeAndEndTime(
            DATE_TIME_1, DATE_TIME_2);
        assertEquals(expected, actual);
    }

    @Test
    void whenFindByStartTimeAndEndTime1ApptThen1Appt() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Add 1 appointment
        AppointmentDto appointmentDto = creator.createAppointmentDto(
            storeSlotDto, storeDto, userDto);
        appointmentDto = underTest.upsertAppointment(appointmentDto);

        // Check 0 in db, initial conditions
        assertSlotsSize(1);

        // Verify 1 appointment found
        List<AppointmentDto> expected = ImmutableList.of(appointmentDto);
        List<AppointmentDto> actual = underTest.findByStartTimeAndEndTime(
            DATE_TIME_1, DATE_TIME_2);
        assertEquals(expected, actual);

    }

    @Test
    void whenFindByStartTimeAndEndTimeAndStoreCodeListEmptyThenEmpty() {
        List<AppointmentDto> expected = ImmutableList.of();
        List<AppointmentDto> actual = underTest
            .findByStartTimeAndEndTimeAndStoreCodeList(
                DATE_TIME_1, DATE_TIME_2,
                List.of("non-existent")
            );
        assertEquals(expected, actual);
    }

    @Test
    void whenFindByStartTimeAndEndTimeAndStoreCodeList1ApptThen1Appt() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Add 1 appointment
        AppointmentDto appointmentDto = creator.createAppointmentDto(
            storeSlotDto, storeDto, userDto);
        appointmentDto = underTest.upsertAppointment(appointmentDto);

        // Check 0 in db, initial conditions
        assertSlotsSize(1);

        // Verify 1 appointment found
        List<AppointmentDto> expected = ImmutableList.of(appointmentDto);
        List<AppointmentDto> actual = underTest
            .findByStartTimeAndEndTimeAndStoreCodeList(
                DATE_TIME_1, DATE_TIME_2,
                List.of(storeDto.getStoreCode())
            );
        assertEquals(expected, actual);

    }

    @Test
    void whenFindByStoreAndAppointmentEmptyThenEmpty() {
        List<AppointmentDto> expected = ImmutableList.of();
        List<AppointmentDto> actual = underTest
            .findByStoreAndAppointmentSlot(storeDto, storeSlotDto);
        assertEquals(expected, actual);
    }

    @Test
    void whenFindByStoreAndAppointmentNullStoreThenException() {
        Exception ex = assertThrows(AppointmentException.class,
            () -> underTest
                .findByStoreAndAppointmentSlot(null, storeSlotDto));
        String expected = """
            Null value for find appointment,\s
            store: 'null',\s
            slot: 'StoreSlotDto(id=null, slotCode=test, storeCode=test, startTime=2019-04-01T16:24:11.252Z[UTC], endTime=2019-04-01T16:54:11.252Z[UTC])'\s
            users: '[]'""";
        assertEquals(expected, ex.getMessage());
    }

    @Test
    void whenFindByStoreAndAppointmentNullSlotThenException() {
        Exception ex = assertThrows(AppointmentException.class,
            () -> underTest
                .findByStoreAndAppointmentSlot(storeDto, null));
        String expected = "Null value for find appointment, \n";
        assertTrue(ex.getMessage().startsWith(expected));
    }

    @Test
    void whenFindByStoreAndAppointmentNullStoreNullSlotThenException() {
        Exception ex = assertThrows(AppointmentException.class,
            () -> underTest
                .findByStoreAndAppointmentSlot(null, null));
        String expected = """
            Null value for find appointment,\s
            store: 'null',\s
            slot: 'null'\s
            users: '[]'""";
        assertEquals(expected, ex.getMessage());
    }

    @Test
    void whenUpsertAppointmentCreateThenCreate() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Step 1 - save
        AppointmentDto appointmentDto = creator.createAppointmentDto(
            storeSlotDto,
            storeDto, userDto
        );
        long actual = underTest.upsertAppointment(appointmentDto).getId();
        long expected = 1L;
        assertTrue(expected <= actual);

        // Check 1 in db, therefore created
        assertSlotsSize(1);
    }

    @Test
    void whenUpsertAppointmentExistsThenUpdate() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Step 1 - save
        AppointmentDto appointmentDto = creator.createAppointmentDto(
            storeSlotDto,
            storeDto, userDto
        );
        long actual1 = underTest.upsertAppointment(appointmentDto).getId();
        long expected = 1L;
        assertTrue(expected <= actual1);

        // Check 1 in db, therefore created
        assertSlotsSize(1);

        // Step 2 - Update
        long actual2 = underTest.upsertAppointment(appointmentDto).getId();
        assertEquals(actual1, actual2);

        // Check 1 in db, therefore updated
        assertSlotsSize(1);
    }

    private void assertSlotsSize(int expectedSize) {
        List<AppointmentDto> slots = underTest.findAllAppointments();
        assertEquals(expectedSize, slots.size());
    }

}