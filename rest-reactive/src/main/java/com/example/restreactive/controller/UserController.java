package com.example.restreactive.controller;


import com.example.restreactive.dto.ErrorDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<?> handleException(Exception exception) {
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);
        String message = cause.getClass().getSimpleName() + ": " + cause.getMessage();
        System.out.println("The exception is " + cause);
        return ResponseEntity.badRequest()
            .body(ErrorDto.builder()
                .code("400")
                .message(message)
                .build());
    }

    @GetMapping(value = "/users/{limit}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    Flux<UserDto> listAllUsersWithLimit(@PathVariable int limit) {
        final int max = 1000;
        int limitTemp = (limit < max ? limit : max);
        return Flux
            .fromStream(userService.findAllUsers().stream())
            .take(limitTemp)
            .delayElements(Duration.ofMillis(100));
    }

    @GetMapping(value = "/user/{username}",
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<UserDto> findUser(@PathVariable String username) {
        return Mono.just(
            userService.findByUsername(username)
                .orElseThrow(() -> new AppointmentException("User not found: " + username))
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
