package com.example.restreactive.controller;

import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.mapping.AppointmentException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ControllerExceptionHandler {

    @ExceptionHandler
    ResponseEntity<?> handleException(Exception exception) {
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);
        String message = cause.getClass().getSimpleName() + ": " + cause.getMessage();
        System.out.println("The exception is " + cause);
        if (exception instanceof AppointmentException ap) {
            String message1 = ap.getClass().getSimpleName() + ": " + ap.getMessage();
            return ResponseEntity
                .status(httpStatusOf(ap.getStatusCode()))
                .body(MessageDto.builder()
                    .code("" + ap.getStatusCode())
                    .message(message1)
                    .build());
        }
        return ResponseEntity.badRequest()
            .body(MessageDto.builder()
                .code("400")
                .message(message)
                .build());
    }

    private int httpStatusOf(String statusCode) {
        try{
            return Integer.parseInt(statusCode);
        }catch(Exception e){
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }

}
