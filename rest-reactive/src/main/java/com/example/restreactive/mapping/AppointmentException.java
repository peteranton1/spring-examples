package com.example.restreactive.mapping;

import lombok.Getter;
import lombok.With;
import org.springframework.http.HttpStatus;

@With
@Getter
public class AppointmentException extends RuntimeException {

    private final String statusCode;

    public AppointmentException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public AppointmentException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = String.valueOf(statusCode.value());
    }
}
