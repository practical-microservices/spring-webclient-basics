package com.example.springwebclientbasics;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
@RestController
public class SpringWebclientBasicsApplication  {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebclientBasicsApplication.class, args);
    }

    @GetMapping(value = "/", produces = "text/plain")
    public Flux<String> get() {
        return WebClient.create()
                .get()
                .uri("https://reqres.in/api/users?page=2")
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(s -> Flux.fromIterable((List<String>) JsonPath.parse(s).read("$.data[*].first_name")))
                .flatMap(s -> WebClient.create()
                        .get()
                        .uri("https://devops.datenkollektiv.de/renderBannerTxt?font=soft&text=" + s)
                        .retrieve()
                        .bodyToMono(String.class)
                );

    }
}
