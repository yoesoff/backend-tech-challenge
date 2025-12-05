package com.mhyusuf.gateway.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/public-api")
public class PublicApiController {

    private final WebClient webClient;

    public PublicApiController(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @GetMapping(value = "/listings", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map<String, Object>> getListings(
            @RequestParam(name = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(name = "user_id", required = false) String userId
    ) {
        String listingUrl = "http://listing-service:6000/listings?page_num=" + pageNum + "&page_size=" + pageSize;
        if (userId != null && !userId.isEmpty()) {
            listingUrl += "&user_id=" + userId;
        }

        return webClient.get()
                .uri(listingUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(6))
                .flatMap(listingResp -> {
                    List<Map<String, Object>> listings = (List<Map<String, Object>>) listingResp.getOrDefault("listings", Collections.emptyList());
                    if (listings.isEmpty()) {
                        return Mono.just(Map.of("result", true, "listings", Collections.emptyList()));
                    }

                    List<Mono<Map<String, Object>>> calls = listings.stream()
                            .map(listing -> {
                                Object uid = listing.get("user_id");
                                if (uid == null) {
                                    listing.put("user", null);
                                    return Mono.just(listing);
                                }
                                String userUrl = "http://user-service:8081/users/" + uid;
                                return webClient.get()
                                        .uri(userUrl)
                                        .retrieve()
                                        .bodyToMono(Map.class)
                                        .timeout(Duration.ofSeconds(3))
                                        .map(userResp -> {
                                            // expect userResp contains "user" key as per README
                                            Object userObj = userResp.get("user");
                                            listing.put("user", userObj);
                                            return listing;
                                        })
                                        .onErrorResume(e -> {
                                            listing.put("user", null);
                                            return Mono.just(listing);
                                        });
                            })
                            .collect(Collectors.toList());

                    return Flux.mergeSequential(calls).collectList()
                            .map(finalListings -> Map.of("result", true, "listings", finalListings));
                })
                .onErrorResume(err -> Mono.just(Map.of("result", false, "error", "Unable to fetch listings")));
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map> createUser(@RequestBody Map<String, Object> payload) {
        return webClient.post()
                .uri("http://user-service:8081/users")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(5));
    }

    @PostMapping(value = "/listings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Map> createListing(@RequestBody Map<String, Object> payload) {
        return webClient.post()
                .uri("http://listing-service:6000/listings")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(5));
    }
}
