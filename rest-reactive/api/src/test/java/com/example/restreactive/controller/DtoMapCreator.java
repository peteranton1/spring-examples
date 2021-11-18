package com.example.restreactive.controller;

import com.example.restreactive.dto.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.restreactive.service.EntityDtoCreator.END_TIME_STR_1;
import static com.example.restreactive.service.EntityDtoCreator.START_TIME_STR_1;
import static java.util.Collections.emptyList;

public class DtoMapCreator {

    public Map<String, Object> asMapStoreDto(AppointmentDto appointmentDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (appointmentDto != null) {
            outerMap.put("id", appointmentDto.getId());
            outerMap.put("appointmentCode", appointmentDto.getAppointmentCode());
            outerMap.put("store", null);
            outerMap.put("storeSlot", null);
            outerMap.put("users", emptyList());
        }
        return outerMap;
    }

    public Map<String, Object> asMapStoreDto(StoreDto storeDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (storeDto != null) {
            outerMap.put("id", storeDto.getId());
            outerMap.put("storeName", storeDto.getStoreName());
            outerMap.put("storeCode", storeDto.getStoreCode());
            outerMap.put("address", asMapAddressDto(storeDto.getAddress()));
        }
        return outerMap;
    }

    public Map<String, Object> asMapAddressDto(StreetAddressDto addressDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (addressDto != null) {
            outerMap.put("id", addressDto.getId());
            outerMap.put("line1", addressDto.getLine1());
            outerMap.put("line2", addressDto.getLine2());
            outerMap.put("city", addressDto.getCity());
            outerMap.put("county", addressDto.getCounty());
            outerMap.put("country", asMapCountryDto(addressDto.getCountry()));
            outerMap.put("postcode", addressDto.getPostcode());
        }
        return outerMap;
    }

    public Map<String, Object> asMapCountryDto(CountryDto countryDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (countryDto != null) {
            outerMap.put("id", countryDto.getId());
            outerMap.put("name", countryDto.getName());
            outerMap.put("code", countryDto.getCode());
        }
        return outerMap;
    }

    public Map<String, Object> asMapStoreDto(StoreSlotDto storeSlotDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (storeSlotDto != null) {
            outerMap.put("id", storeSlotDto.getId());
            outerMap.put("slotCode", storeSlotDto.getSlotCode());
            outerMap.put("storeCode", storeSlotDto.getStoreCode());
            outerMap.put("startTime", START_TIME_STR_1);
            outerMap.put("endTime", END_TIME_STR_1);
        }
        return outerMap;
    }

    public Map<String, Object> asMapUserDto(UserDto userDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (userDto != null) {
            outerMap.put("id", userDto.getId());
            outerMap.put("username", userDto.getUsername());
            outerMap.put("firstName", userDto.getFirstName());
            outerMap.put("lastName", userDto.getLastName());
            outerMap.put("email", asMapEmailDto(userDto.getEmail()));
        }
        return outerMap;
    }

    public Map<String, Object> asMapEmailDto(EmailAddressDto emailDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (emailDto != null) {
            outerMap.put("id", emailDto.getId());
            outerMap.put("email", emailDto.getEmail());
        }
        return outerMap;
    }
}
