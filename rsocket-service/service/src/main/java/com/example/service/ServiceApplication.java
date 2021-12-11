package com.example.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ClientHealthState {
    private boolean healthy;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingResponse {
    private String message;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingRequest {
    public String name;
}

@Configuration
class SecurityConfiguration {

    @Bean
    PayloadSocketAcceptorInterceptor autherization(
        RSocketSecurity security) {
        return security
            .authorizePayload(ap -> ap.anyExchange().authenticated())
            .simpleAuthentication(Customizer.withDefaults())
            // .jwt()
            .build();
    }

    @Bean
    MapReactiveUserDetailsService authentication() {
        return new MapReactiveUserDetailsService(
            User.withDefaultPasswordEncoder()
                .username("jlong")
                .password("pw")
                .roles("role")
                .build());
    }

    @Bean
    RSocketMessageHandler messageHandler(
        RSocketStrategies strategies) {
        var rmh = new RSocketMessageHandler();
        rmh.getArgumentResolverConfigurer()
            .addCustomResolver(
                new AuthenticationPrincipalArgumentResolver());
        rmh.setRSocketStrategies(strategies);
        return rmh;
    }
}

@Controller
class GreetingController {

    @MessageMapping("greetings")
    Flux<GreetingResponse> greet(
        RSocketRequester clientRsocketConnection,
        @AuthenticationPrincipal Mono<UserDetails> user) {
        return user.map(UserDetails::getUsername)
            .map(GreetingRequest::new)
            .flatMapMany(gr ->
                this.greetNoSecurity(clientRsocketConnection, gr));
    }

    @MessageMapping("greetingsNoSecurity")
    private Flux<GreetingResponse> greetNoSecurity(
        RSocketRequester clientRsocketConnection,
        GreetingRequest request) {

        var in = clientRsocketConnection
            .route("health")
            .retrieveFlux(ClientHealthState.class)
            .filter(chs -> !chs.isHealthy());

        var out = Flux
            .fromStream(Stream.generate(
                () -> new GreetingResponse("ni hao " +
                    request.name +
                    " @ " + Instant.now())))
            .take(20)
            .delayElements(Duration.ofSeconds(1));

        return out.takeUntilOther(in);
    }
}