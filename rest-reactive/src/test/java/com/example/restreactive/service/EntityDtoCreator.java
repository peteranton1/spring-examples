package com.example.restreactive.service;

import com.example.restreactive.dto.*;
import com.example.restreactive.model.*;
import com.google.common.collect.ImmutableList;

import java.time.ZonedDateTime;

public class EntityDtoCreator {

    public Appointment createAppointment(
        AppointmentSlot appointmentSlot,
        Store store,
        User user) {
        return Appointment.builder()
            .appointmentSlot(appointmentSlot)
            .store(store)
            .users(ImmutableList.of(user))
            .build();
    }

    public AppointmentDto createAppointmentDto(
        AppointmentSlotDto appointmentSlotDto,
        StoreDto storeDto,
        UserDto userDto) {
        return AppointmentDto.builder()
            .appointmentSlotDto(appointmentSlotDto)
            .store(storeDto)
            .users(ImmutableList.of(userDto))
            .build();
    }

    public AppointmentSlot createAppointmentSlot(ZonedDateTime startTime,
                                                 ZonedDateTime endTime) {
        return AppointmentSlot.builder()
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }

    public AppointmentSlotDto createAppointmentSlotDto(ZonedDateTime startTime,
                                                       ZonedDateTime endTime) {
        return AppointmentSlotDto.builder()
            .startTime(startTime)
            .endTime(endTime)
            .build();
    }

    public Store createStore(StreetAddress streetAddress) {
        return Store.builder()
            .storeName("mystore")
            .storeCode("mycode")
            .address(streetAddress)
            .build();
    }

    public StoreDto createStoreDto(StreetAddressDto streetAddressDto) {
        return StoreDto.builder()
            .storeName("mystore")
            .storeCode("mycode")
            .address(streetAddressDto)
            .build();
    }

    public StreetAddress createStreetAddress(Country country) {
        return StreetAddress.builder()
            .line1("line1")
            .line2("line2")
            .city("city")
            .county("county")
            .country(country)
            .postcode("postcode")
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

    public Country createCountry() {
        return Country.builder()
            .name("name")
            .code("code")
            .build();
    }

    public CountryDto createCountryDto() {
        return CountryDto.builder()
            .name("name")
            .code("code")
            .build();
    }

    public User createUser(EmailAddress emailAddress) {
        return User.builder()
            .username("myusername")
            .email(emailAddress)
            .build();
    }

    public UserDto createUserDto(EmailAddressDto emailAddressDto) {
        return UserDto.builder()
            .username("myusername")
            .email(emailAddressDto)
            .build();
    }

    public EmailAddress createEmailAddress(String email) {
        return EmailAddress.builder()
            .email(email)
            .build();
    }

    public EmailAddressDto createEmailAddressDto(String email) {
        return EmailAddressDto.builder()
            .email(email)
            .build();
    }
}

