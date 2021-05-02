package com.agincourt.chesskafkaproducer.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Bean;

public class LichessResponsePreparer {
    @Bean
    public static JsonNode reshapeLichessJson(JsonNode jsonNode) {
        ((ObjectNode) jsonNode).set("lichessId", jsonNode.get("id"));
        ((ObjectNode) jsonNode).remove("id");
        ((ObjectNode) jsonNode).set("whitePlayerId", jsonNode.get("players").get("white").get("user").get("id"));
        ((ObjectNode) jsonNode).set("whitePlayerRating", jsonNode.get("players").get("white").get("rating"));
        ((ObjectNode) jsonNode).set("whitePlayerRatingDiff", jsonNode.get("players").get("white").get("ratingDiff"));
        ((ObjectNode) jsonNode).set("blackPlayerId", jsonNode.get("players").get("black").get("user").get("id"));
        ((ObjectNode) jsonNode).set("blackPlayerRating", jsonNode.get("players").get("black").get("rating"));
        ((ObjectNode) jsonNode).set("blackPlayerRatingDiff", jsonNode.get("players").get("black").get("ratingDiff"));
        ((ObjectNode) jsonNode).remove("players");

        ((ObjectNode) jsonNode).set("openingECO", jsonNode.get("opening").get("eco"));
        ((ObjectNode) jsonNode).set("openingName", jsonNode.get("opening").get("name"));
        ((ObjectNode) jsonNode).set("openingPly", jsonNode.get("opening").get("ply"));
        ((ObjectNode) jsonNode).remove("opening");

        ((ObjectNode) jsonNode).set("clockInitial", jsonNode.get("clock").get("initial"));
        ((ObjectNode) jsonNode).set("clockIncrement", jsonNode.get("clock").get("increment"));
        ((ObjectNode) jsonNode).set("clockTotalTime", jsonNode.get("clock").get("totalTime"));
        ((ObjectNode) jsonNode).remove("clock");

        return jsonNode;
    }
}
