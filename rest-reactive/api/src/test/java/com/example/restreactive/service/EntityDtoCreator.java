package com.example.restreactive.service;

import com.example.restreactive.dto.*;
import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.model.User;
import com.google.common.collect.ImmutableList;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class EntityDtoCreator {

    public static final String START_TIME_STR_1 = "2019-04-01T16:24:11.252Z";
    public static final String END_TIME_STR_1 = "2019-04-01T16:54:11.252Z";
    public static final ZonedDateTime START_TIME_1 = ZonedDateTime
        .parse(START_TIME_STR_1).withZoneSameLocal(ZoneId.of("UTC"));
    public static final ZonedDateTime END_TIME_1 = ZonedDateTime
        .parse(END_TIME_STR_1).withZoneSameLocal(ZoneId.of("UTC"));

    public Appointment createAppointment(
        StoreSlot storeSlot,
        Store store,
        User user) {
        return Appointment.builder()
            .storeSlot(storeSlot)
            .store(store)
            .users(ImmutableList.of(user))
            .build();
    }

    public AppointmentDto createAppointmentDto(
        StoreSlotDto storeSlotDto,
        StoreDto storeDto,
        UserDto userDto) {
        return AppointmentDto.builder()
            .storeSlot(storeSlotDto)
            .store(storeDto)
            .users(ImmutableList.of(userDto))
            .build();
    }


    public StoreDto getStoreDto(int i) {
        String storeCode = "store" + i;
        return StoreDto.builder()
            .id(i)
            .storeCode(storeCode)
            .storeName(storeCode)
            .address(getStreetAddressDto(i))
            .build();
    }

    public StreetAddressDto getStreetAddressDto(int i) {
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

    public CountryDto getCountryDto(int i) {
        String name = "country" + i;
        return CountryDto.builder()
            .id(i)
            .name(name)
            .code(name)
            .build();
    }

    public StoreSlotDto getStoreSlotDto(int i) {
        String slotCode = "slot" + i;
        String storeCode = "store" + i;
        return createStoreSlotDto(
            slotCode, storeCode, START_TIME_1, END_TIME_1);
    }

    public StoreSlotDto createStoreSlotDto(
        String slotCode,
        String storeCode,
        ZonedDateTime startTime,
        ZonedDateTime endTime) {
        return StoreSlotDto.builder()
            .slotCode(slotCode)
            .storeCode(storeCode)
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }

    public StoreDto createStoreDto(StreetAddressDto streetAddressDto) {
        return StoreDto.builder()
            .storeName("mystore")
            .storeCode("mycode")
            .address(streetAddressDto)
            .build();
    }

    public StreetAddressDto createStreetAddressDto(CountryDto countryDto) {
        return StreetAddressDto.builder()
            .line1("line1")
            .line2("line2")
            .city("city")
            .county("county")
            .country(countryDto)
            .postcode("postcode")
            .build();
    }

    public CountryDto createCountryDto() {
        return CountryDto.builder()
            .name("name")
            .code("code")
            .build();
    }

    public UserDto getUserDto(int i) {
        return createUserDto(createEmailAddressDto("a@a.a"));
    }

    public UserDto createUserDto(EmailAddressDto emailAddressDto) {
        return UserDto.builder()
            .username("myusername")
            .email(emailAddressDto)
            .build();
    }

    public EmailAddressDto createEmailAddressDto(String email) {
        return EmailAddressDto.builder()
            .email(email)
            .build();
    }

    public AppointmentDto getAppointmentDto(
        StoreDto storeDto,
        StoreSlotDto storeSlotDto,
        UserDto userDto
    ) {
        return AppointmentDto.builder()
            .appointmentCode(storeSlotDto.getSlotCode())
            .store(storeDto)
            .storeSlot(storeSlotDto)
            .users(List.of(userDto))
            .build();
    }
}

