package com.example.restreactive.service;

import com.example.restreactive.dto.AppointmentDto;
import com.example.restreactive.dto.AppointmentSlotDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.model.AppointmentSlot;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.AppointmentRepository;
import com.example.restreactive.repository.AppointmentSlotRepository;
import com.example.restreactive.repository.StoreRepository;
import com.example.restreactive.repository.UserRepository;
import com.google.common.collect.ImmutableList;
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

    @Autowired
    private AppointmentService underTest;

    @Autowired
    private AppointmentSlotService appointmentSlotService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private EntityDtoCreator creator = new EntityDtoCreator();

    private AppointmentSlotDto appointmentSlotDto ;

    private StoreDto storeDto ;

    private UserDto userDto ;


    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();

        // Appointment slot
        appointmentSlotDto = creator
            .createAppointmentSlotDto(DATE_TIME_1, DATE_TIME_2);
        appointmentSlotService.upsertAppointmentSlot(appointmentSlotDto);

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

    @Test
    void whenFindAllSizeThenEmpty() {
        assertSlotsSize(0);
    }

    @Test
    void whenFindByStoreAndAppointmentEmptyThenEmpty() {
        List<AppointmentDto> expected = ImmutableList.of();
        List<AppointmentDto> actual = underTest
            .findByStoreAndAppointmentSlot(storeDto, appointmentSlotDto);
        assertEquals(expected, actual);
    }

    @Test
    void whenFindByStoreAndAppointmentNullStoreThenException() {
        Exception ex = assertThrows(AppointmentException.class,
            () -> underTest
            .findByStoreAndAppointmentSlot(null, appointmentSlotDto));
        String expected = """
            Null value for find appointment,\s
            store: 'null',\s
            slot: 'AppointmentSlotDto(id=null, startTime=2019-04-01T16:24:11.252Z, endTime=2019-04-01T16:54:11.252Z)'""";
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
            slot: 'null'""";
        assertEquals(expected, ex.getMessage());
    }

    @Test
    void whenUpsertAppointmentCreateThenCreate() {

        // Check 0 in db, initial conditions
        assertSlotsSize(0);

        // Step 1 - save
        AppointmentDto appointmentDto = creator.createAppointmentDto(
            appointmentSlotDto,
            storeDto, userDto
        );
        long actual = underTest.upsertAppointment(appointmentDto);
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
            appointmentSlotDto,
            storeDto, userDto
        );
        long actual1 = underTest.upsertAppointment(appointmentDto);
        long expected = 1L;
        assertTrue(expected <= actual1);

        // Check 1 in db, therefore created
        assertSlotsSize(1);

        // Step 2 - Update
        long actual2 = underTest.upsertAppointment(appointmentDto);
        assertEquals(actual1, actual2);

        // Check 1 in db, therefore updated
        assertSlotsSize(1);
    }

    private void assertSlotsSize(int expectedSize) {
        List<AppointmentDto> slots = underTest.findAllAppointments();
        assertEquals(expectedSize, slots.size());
    }

}