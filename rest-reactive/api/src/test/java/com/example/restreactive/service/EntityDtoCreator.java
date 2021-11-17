package com.example.restreactive.service;

import com.example.restreactive.dto.*;
import com.example.restreactive.model.Appointment;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.model.User;
import com.google.common.collect.ImmutableList;

import java.time.ZonedDateTime;

public class EntityDtoCreator {

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
}

