package com.example.restreactive.mapping;

public class AppointmentException extends RuntimeException {

    public AppointmentException() {
    }

    public AppointmentException(String message) {
        super(message);
    }

    public AppointmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppointmentException(Throwable cause) {
        super(cause);
    }

    public AppointmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
