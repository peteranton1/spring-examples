package com.example.restreactive.controller;


import com.example.restreactive.dto.ErrorDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ExceptionHandler
    ResponseEntity<?> handleException(Exception exception) {
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);
        String message = cause.getClass().getSimpleName() + ": " + cause.getMessage();
        System.out.println("The exception is " + cause);
        if (exception instanceof AppointmentException ap) {
            String message1 = ap.getClass().getSimpleName() + ": " + ap.getMessage();
            return ResponseEntity
                .status(httpStatusOf(ap.getStatusCode()))
                .body(ErrorDto.builder()
                    .code("" + ap.getStatusCode())
                    .message(message1)
                    .build());
        }
        return ResponseEntity.badRequest()
            .body(ErrorDto.builder()
                .code("400")
                .message(message)
                .build());
    }

    private int httpStatusOf(String statusCode) {
        try{
            return Integer.parseInt(statusCode);
        }catch(Exception e){
            return HttpStatus.SERVICE_UNAVAILABLE.value();
        }
    }

    @GetMapping(value = "/users/{limit}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<UserDto> listAllUsersWithLimit(@PathVariable int limit) {
        final int max = 1000;
        int limitTemp = (limit < max ? limit : max);
        List<UserDto> userDtos = userService.findAllUsers();
        log.info("Controller: users {} ", userDtos.size());
        return Flux
            .fromStream(userDtos.stream())
            .take(limitTemp)
            //.delayElements(Duration.ofMillis(100))
            ;
    }

    @GetMapping(value = "/user/{username}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<UserDto> findUser(@PathVariable String username) {
        AppointmentException ap = new AppointmentException(
            "User not found: " + username,
            HttpStatus.NOT_FOUND);
        return Mono.just(
            userService.findByUsername(username)
                .orElseThrow(() -> ap)
        );
    }

    @PutMapping(value = "/user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<UserDto> upsertUser(@RequestBody UserDto request) {
        return Mono.just(
            userService.upsertUser(request));
    }

}
