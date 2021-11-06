package com.example.restreactive.controller;


import com.example.restreactive.greeting.Greeting;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Stream;

@RestController
public class UserController {

    @ExceptionHandler
        //@ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<?> handleException(Exception exception) {
        System.out.println("The exception is " +
            NestedExceptionUtils.getMostSpecificCause(exception));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/user/{username}")
    Mono<Greeting> userMono(@PathVariable String username) {
        return Mono.just(new Greeting(username));
    }

    @GetMapping(value = "/users",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Greeting> userFlux() {
        return Flux
            .fromStream(Stream.generate(() ->
                new Greeting("Hello, World!")))
            .take(10)
            .delayElements(Duration.ofSeconds(1));
    }
}
