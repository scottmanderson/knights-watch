package com.agincourt.chesskafkaproducer.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;

@Service
public class LichessClient {

    public static final String API_ROOT = "https://lichess.org/api";

    @Value("${lichessHandle}")
    private String lichessHandle;

    public LichessClient() {
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restBuild) {
        return restBuild.build();
    }


    private static String lichessRequestUriStringConstructor(String lichessHandle, HashMap<String, String> options) {
        String queryUri = API_ROOT + "/games/user/" + lichessHandle + "?";
        // query string construction:  first option below must omit ampersand
        queryUri += "rated=" + options.getOrDefault("rated", "true");
        queryUri += "&variant=" + options.getOrDefault("variant", "standard");
        queryUri += "&perfType=" + options.getOrDefault("perfType", "blitz");
        queryUri += "&opening=" + options.getOrDefault("opening", "true");
        queryUri += "&pgnInJson=" + options.getOrDefault("pgnInJson", "true");
        queryUri += "&clocks=" + options.getOrDefault("clocks", "true");
        queryUri += "&evals=" + options.getOrDefault("evals", "false");
        queryUri += "&since=" + options.getOrDefault("since", "Account creation date");
        queryUri += "&until=" + options.getOrDefault("until", "Now");
        queryUri += "&max=" + options.getOrDefault("max", "null");

        System.out.println(queryUri);
        return queryUri;
    }

    public static ResponseEntity<String> fetchGamesForUser(RestTemplate restTemplate, String lichessHandle, HashMap<String, String> options) {
        String queryUri = lichessRequestUriStringConstructor(lichessHandle, options);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_NDJSON));
        final HttpEntity<String> request = new HttpEntity<String>(headers);
        return restTemplate.exchange(queryUri, HttpMethod.GET, request, String.class);
    }

}
