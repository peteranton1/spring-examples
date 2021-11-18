package com.example.restreactive.controller;

import com.example.restreactive.dto.*;
import com.example.restreactive.mapping.*;
import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.AppointmentRepository;
import com.example.restreactive.repository.StoreRepository;
import com.example.restreactive.repository.StoreSlotRepository;
import com.example.restreactive.repository.UserRepository;
import com.example.restreactive.service.AppointmentService;
import com.example.restreactive.service.EntityDtoCreator;
import com.example.restreactive.service.StoreService;
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

import static com.example.restreactive.service.EntityDtoCreator.END_TIME_STR_1;
import static com.example.restreactive.service.EntityDtoCreator.START_TIME_STR_1;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {AppointmentController.class})
@Import({AppointmentService.class,
    ModelMapper.class,
    AppointmentMapper.class,
    StoreMapper.class,
    StreetAddressMapper.class,
    CountryMapper.class,
    EmailAddressMapper.class,
    StoreSlotMapper.class,
    UserMapper.class,
    StoreSlotRepository.class,
})
class AppointmentControllerTest {

    private static final String SLOT_1 = "slot1";
    private static final String STORE_1 = "store1";

    private static final String PUT_APPOINTMENT = "/appointment";
    private static final String DELETE_APPOINTMENT = "/appointment/{appointmentCode}";
    private static final String APPOINTMENT_START_TIME_LIMIT =
        "/appointments/" + START_TIME_STR_1 + "/10";
    private static final String PUT_STORE_STORE_CODE_SLOT =
        "/appointment";
    private static final String DELETE_STORE_STORE_CODE_SLOT_SLOT_CODE =
        "/appointment/" + STORE_1 + "-" + SLOT_1;

    private static final ZonedDateTime START_TIME_1 = ZonedDateTime
        .parse(START_TIME_STR_1).withZoneSameLocal(ZoneId.of("UTC"));

    private static final ZonedDateTime END_TIME_1 = ZonedDateTime
        .parse(END_TIME_STR_1).withZoneSameLocal(ZoneId.of("UTC"));


    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private StoreSlotRepository storeSlotRepository;

    @MockBean
    private StoreService storeService;

    @MockBean
    private UserRepository userRepository;

    private final EntityDtoCreator dtoCreator = new EntityDtoCreator();
    private final DtoMapCreator mapCreator = new DtoMapCreator();

    @Test
    void whenListAllSlotsForStartDateWithLimitAndEmptyThenEmpty() {
        when(appointmentRepository.findAllAppointmentsBetweenStartTimeAndEndTime(
            START_TIME_1, END_TIME_1))
            .thenReturn(emptyList());

        List<?> actual = webTestClient.get()
            .uri(APPOINTMENT_START_TIME_LIMIT)
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
        AppointmentDto appointmentDto = getAppointmentDto(1);
        Appointment appointment = (Appointment) modelMapper.toEntity(appointmentDto);

        when(appointmentRepository.findAllAppointmentsBetweenStartTimeAndEndTime(
            any(), any()))
            .thenReturn(ImmutableList.of(appointment));

        List<?> actual = webTestClient.get()
            .uri(APPOINTMENT_START_TIME_LIMIT)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = ImmutableList.of(mapCreator.asMapStoreDto(appointmentDto));
        assertEquals(expected, actual);
    }

    @Test
    void whenUpsertAppointmentInsertThenOk() {
        StoreDto storeDto = dtoCreator.getStoreDto(1);
        Store store = (Store) modelMapper.toEntity(storeDto);
        StoreSlotDto storeSlotDto = dtoCreator.getStoreSlotDto(1);
        StoreSlot storeSlot = (StoreSlot) modelMapper.toEntity(storeSlotDto);
        UserDto userDto = dtoCreator.getUserDto(1);
        User user = (User) modelMapper.toEntity(userDto);
        AppointmentDto appointmentDto = dtoCreator.getAppointmentDto(storeDto, storeSlotDto, userDto);
        Appointment appointment = (Appointment) modelMapper.toEntity(appointmentDto);

        when(appointmentRepository
            .findAllAppointmentsByStoresListBetweenStartTimeAndEndTime(
            any(), any(), any()))
            .thenReturn(ImmutableList.of(appointment));
        when(storeSlotRepository
            .findAllSlotsBetweenStartTimeAndEndTime(
            any(), any()))
            .thenReturn(ImmutableList.of(storeSlot));
        when(storeService
            .findByStoreCode(any()))
            .thenReturn(ImmutableList.of(store));
        when(userRepository
            .findByUsername(any()))
            .thenReturn(ImmutableList.of(user));
        when(appointmentRepository.save(any()))
            .thenReturn(appointment);

        AppointmentDto actual = webTestClient.put()
            .uri(PUT_STORE_STORE_CODE_SLOT)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(appointmentDto), AppointmentDto.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(AppointmentDto.class)
            .returnResult().getResponseBody();

        //AppointmentDto expected = asMapStoreDto(appointmentDto);
        assertEquals(appointmentDto, actual);
    }

    @Test
    void whenDeleteStoreNotExistsThenOk() {
        MessageDto messageDto = MessageDto.builder()
            .code("200")
            .message("Appointment not found: " + STORE_1 + "-" + SLOT_1)
            .build();
        when(appointmentRepository.findByAppointmentCode(
            any()))
            .thenReturn(ImmutableList.of());

        MessageDto actual = webTestClient.delete()
            .uri(DELETE_STORE_STORE_CODE_SLOT_SLOT_CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        assertEquals(messageDto, actual);
    }

    @Test
    void whenDeleteStoreExistsThenOk() {
        StoreDto storeDto = dtoCreator.getStoreDto(1);
        StoreSlotDto storeSlotDto = dtoCreator.getStoreSlotDto(1);
        UserDto userDto = dtoCreator.getUserDto(1);
        AppointmentDto appointmentDto = dtoCreator.getAppointmentDto(storeDto, storeSlotDto, userDto);
        Appointment appointment = (Appointment) modelMapper.toEntity(appointmentDto);
        MessageDto messageDto = MessageDto.builder()
            .code("200")
            .message("Appointment deleted: " + SLOT_1)
            .build();

        when(appointmentRepository.findByAppointmentCode(any()))
            .thenReturn(ImmutableList.of(appointment));

        MessageDto actual = webTestClient.delete()
            .uri(DELETE_STORE_STORE_CODE_SLOT_SLOT_CODE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        assertEquals(messageDto, actual);
    }

    private AppointmentDto getAppointmentDto(int i) {
        return AppointmentDto.builder()
            .id(i)
            .appointmentCode("appointment" + i)
            .build();
    }

}