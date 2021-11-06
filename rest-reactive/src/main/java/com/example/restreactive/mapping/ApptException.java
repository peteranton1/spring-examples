package com.example.restreactive.mapping;

public class ApptException extends RuntimeException{
    public ApptException() {
    }

    public ApptException(String message) {
        super(message);
    }

    public ApptException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApptException(Throwable cause) {
        super(cause);
    }

    public ApptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
