package com.example.restreactive.controller;


import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
public class UserController extends ControllerExceptionHandler {

    public static final String GET_USERS_LIMIT = "/users/{limit}";
    public static final String GET_USER_USERNAME = "/user/{username}";
    public static final String PUT_USER = "/user";
    public static final String DELETE_USER_USERNAME = "/user/{username}";

    @Autowired
    private UserService userService;

    @GetMapping(value = GET_USERS_LIMIT,
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

    @GetMapping(value = GET_USER_USERNAME,
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

    @PutMapping(value = PUT_USER,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<UserDto> upsertUser(@RequestBody UserDto request) {
        return Mono.just(
            userService.upsertUser(request));
    }

    @DeleteMapping(value = DELETE_USER_USERNAME,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    Mono<MessageDto> deleteUser(@PathVariable String username) {
        return Mono.just(
            userService.deleteUser(username));
    }

}
