package com.example.demo;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BridgeService {

    public static int registerCallbackUrl(String token) throws Exception {
        String callbackUrl = "https://webhook.site/28fd47be-0960-47b4-a17d-396064284a4b";
        WebClient webClient = WebClient.builder()
                .baseUrl("https://dev.ndhm.gov.in")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();

        String requestBody = "{\"url\": \"" + callbackUrl + "\"}";

        Mono<Integer> responseMono = webClient.patch()
                .uri("/devservice/v1/bridges")
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .exchangeToMono(clientResponse -> {
                    // Log response details for debugging purposes
                    System.out.println("RESPONSE : " + clientResponse.statusCode().toString());
                    // Return the status code as an integer
                    return Mono.just(clientResponse.statusCode().value());
                });

        // Block to get the response status code synchronously, handle exceptions outside this method
        return responseMono.block();
    }
}

