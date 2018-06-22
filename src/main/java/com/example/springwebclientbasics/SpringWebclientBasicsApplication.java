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
public class SpringWebclientBasicsApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebclientBasicsApplication.class, args);
    }

    @GetMapping("/")
    public Mono<ServerResponse> get() {
        return ServerResponse.ok().body(WebClient.create()
                .get()
                .uri("https://reqres.in/api/users?page=2")
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(s -> Flux.fromIterable((List<String>)JsonPath.parse(s).read("$.data[*].first_name")))
                .flatMap(s -> WebClient.create()
                        .get()
                        .uri("https://devops.datenkollektiv.de/renderBannerTxt?font=soft&text=" + s)
                        .retrieve()
                        .bodyToMono(String.class)
                ), String.class);

    }
    //access command line arguments
    @Override
    public void run(String... args) throws Exception {


        WebClient.create()
                .get()
                .uri("https://reqres.in/api/users?page=2")
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(s -> Flux.fromIterable((List<String>)JsonPath.parse(s).read("$.data[*].first_name")))
                .flatMap(s -> WebClient.create()
                        .get()
                        .uri("https://devops.datenkollektiv.de/renderBannerTxt?font=soft&text=" + s)
                        .retrieve()
                        .bodyToMono(String.class))
                .doOnNext(s -> System.out.println(s))
                .blockLast();

    }


}
