package com.example.restreactive.greeting;

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
//@Controller
//@ResponseBody
public class GreetingRestController {

    @ExceptionHandler
        //@ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<?> handleException(Exception exception) {
        System.out.println("The exception is " +
            NestedExceptionUtils.getMostSpecificCause(exception));
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/greeting/{name}")
    Mono<Greeting> greetingMono(@PathVariable String name) {
        Assert.isTrue(Character.isUpperCase(name.charAt(0)),
            () -> "The name must be uppercase, " +
                "you supplied: name = " + name);
        return Mono.just(new Greeting(name));
    }

    @GetMapping(value = "/greetings/{name}",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Greeting> greetingFlux(@PathVariable String name) {
        return Flux
            .fromStream(Stream.generate(() ->
                new Greeting("Hello, " + name + "!")))
            .take(10)
            .delayElements(Duration.ofSeconds(1));
    }
}
