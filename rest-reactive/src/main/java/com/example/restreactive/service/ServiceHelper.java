package com.example.restreactive.service;

import com.example.restreactive.mapping.AppointmentException;

import java.util.Objects;

public class ServiceHelper {

    public void assertNonNull(String name, Object value) {
        if(Objects.isNull(value)){
            throw new AppointmentException(String
                .format("assertNonNull: '%s' was null", name));
        }
    }
}
