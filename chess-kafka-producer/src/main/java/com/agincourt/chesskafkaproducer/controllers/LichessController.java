package com.agincourt.chesskafkaproducer.controllers;

import com.agincourt.chesskafkaproducer.engine.Producer;
import com.agincourt.chesskafkaproducer.extraction.LichessClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

import static com.agincourt.chesskafkaproducer.extraction.LichessResponsePreparer.reshapeLichessJson;

@RestController
@RequestMapping(value = "/lichess")
public class LichessController {

    private final Producer producer;
    private final RestTemplate restTemplate;

    @Value("${lichessHandle}")
    private String lichessHandle;

    LichessController(RestTemplate restTemplate, Producer producer) {
        this.producer = producer;
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = "/manual")
    public void processManualApiRequest(
            @RequestParam(value = "rated", required = false) String rated,
            @RequestParam(value = "variant", required = false) String variant,
            @RequestParam(value = "perfType", required = false) String perfType,
            @RequestParam(value = "opening", required = false) String opening,
            @RequestParam(value = "pgnInJson", required = false) String pgnInJson,
            @RequestParam(value = "clocks", required = false) String clocks,
            @RequestParam(value = "evals", required = false) String evals,
            @RequestParam(value = "since", required = false) String since,
            @RequestParam(value = "until", required = false) String until,
            @RequestParam(value = "max", required = false) String max
    ) throws JsonProcessingException {
        HashMap<String, String> options = new HashMap<>();
        if (rated != null) options.put("rated", rated);
        if (variant != null) options.put("variant", variant);
        if (perfType != null) options.put("perfType", perfType);
        if (opening != null) options.put("opening", opening);
        if (pgnInJson != null) options.put("pgnInJson", pgnInJson);
        if (clocks != null) options.put("clocks", clocks);
        if (evals != null) options.put("evals", evals);
        if (since != null) options.put("since", since);
        if (until != null) options.put("until", until);
        if (max != null) options.put("max", max);

        ResponseEntity<String> response = LichessClient.fetchGamesForUser(restTemplate, lichessHandle, options);
        String[] gameStringJSONArray = Objects.requireNonNull(response.getBody()).split("\n");
        for (String s : gameStringJSONArray) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(s);
            reshapeLichessJson(jsonNode);
            sendMessageToKafkaTopic(jsonNode.get("lichessId").toString(), jsonNode.toString());
        }
    }

    private void sendMessageToKafkaTopic(String key, String message) {
        this.producer.sendMessage(key, message);
    }

}
