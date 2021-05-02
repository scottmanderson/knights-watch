package com.agincourt.chesskafkaproducer;

import com.agincourt.chesskafkaproducer.engine.Producer;
import com.agincourt.chesskafkaproducer.extraction.LichessClient;

import com.agincourt.chesskafkaproducer.extraction.LichessResponsePreparer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ChessKafkaProducerApplication {

    private static final Logger log = LoggerFactory.getLogger(ChessKafkaProducerApplication.class);
    private final Producer producer;
    private final Timer timer;

    @Value("${lichessHandle}")
    private String lichessHandle;

    public ChessKafkaProducerApplication(Producer producer) {
        this.producer = producer;
        this.timer = new Timer();
    }

    public void sendMessageToKafkaTopic(String key, String message) {
        this.producer.sendMessage(key, message);
    }

    public void makeNewUpdate(RestTemplate restTemplate, String lichessHandle, HashMap<String, String> options) throws JsonProcessingException {
        // Populate Since Option with Epoch Time
        ResponseEntity<String> response = LichessClient.fetchGamesForUser(restTemplate, lichessHandle, options);
        if (response.hasBody()) {
            String[] gameStringJSONArray = Objects.requireNonNull(response.getBody()).split("\n");
            for (String s : gameStringJSONArray) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(s);
                LichessResponsePreparer.reshapeLichessJson(jsonNode);
                sendMessageToKafkaTopic(jsonNode.get("lichessId").toString(), jsonNode.toString());
            }
        }
    }

    public void updateRolling(RestTemplate restTemplate, String lichessHandle, HashMap<String, String> options, Long rollWindowSeconds) throws JsonProcessingException {
        long since = Instant.now().minus(rollWindowSeconds, ChronoUnit.SECONDS).toEpochMilli();
        options.put("since", Long.toString(since));
        makeNewUpdate(restTemplate, lichessHandle, options);
    }

    public static void main(String[] args) {
        SpringApplication.run(ChessKafkaProducerApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) {
        return args -> {
            HashMap<String, String> options = new HashMap<>();
            UpdateTask task = new UpdateTask(restTemplate, lichessHandle, options, 300L);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(task, 0, 300, TimeUnit.SECONDS);
        };
    }

    class UpdateTask extends TimerTask {
        private final RestTemplate restTemplate;
        private final String lichessHandle;
        private final HashMap<String, String> options;
        private final long rollWindowSeconds;

        public UpdateTask(RestTemplate restTemplate, String lichessHandle, HashMap<String, String> options, long rollWindowSeconds) {
            this.restTemplate = restTemplate;
            this.lichessHandle = lichessHandle;
            this.options = options;
            this.rollWindowSeconds = rollWindowSeconds;
        }

        public void run() {
            try {
                updateRolling(restTemplate, lichessHandle, options, rollWindowSeconds);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
