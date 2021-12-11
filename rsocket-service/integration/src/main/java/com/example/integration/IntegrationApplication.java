package com.example.integration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.rsocket.ClientRSocketConnector;
import org.springframework.integration.rsocket.RSocketInteractionModel;
import org.springframework.integration.rsocket.dsl.RSockets;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Flux;

import java.io.File;


@SpringBootApplication
public class IntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationApplication.class, args);
    }

    @Bean
    ClientRSocketConnector clientRSocketConnector(
        RSocketStrategies strategies){

        var clc = new ClientRSocketConnector("localhost", 8888);
        clc.setRSocketStrategies(strategies);
        return clc;
    }

    // file data | rsocket | log

    @Bean
    MessageChannel reactiveMessageChannel() {
        return MessageChannels.flux().get();
    }

    @Bean
    IntegrationFlow rsocketFlow(
        ClientRSocketConnector connector,
        @Value("${user.home}") File home) {

        // File inbound adapter
        var folder = new File(new File(home, "dev"), "in");
        var fileReadingMessageSource = Files
            .inboundAdapter(folder)
            .autoCreateDirectory(true)
            .get();

        // rsocket outbound gateway
        var rsocket = RSockets
            .outboundGateway("greetings")
            .interactionModel(RSocketInteractionModel.requestStream)
            .clientRSocketConnector(connector)
            .expectedResponseType(GreetingResponse.class);

        return IntegrationFlows
            .from(fileReadingMessageSource,
                pmc -> pmc.poller(pm -> pm.fixedRate(1_000)))
            .transform(new FileToStringTransformer())
            .transform(String.class,
                name -> new GreetingRequest(name.trim()))
            .handle(rsocket)
            .split()
            .channel(reactiveMessageChannel())
            .handle((GenericHandler<GreetingResponse>) (
                payload, headers) -> {
                System.out.println("new Message: " +
                    payload.toString());
                return null;
            })
            .get();
    }
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
