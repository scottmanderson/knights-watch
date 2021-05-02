package com.agincourt.chessstat.util;

import com.agincourt.chessstat.models.ChessGame;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;
import java.util.List;

public class Formatting {

    public static String jsonifyGames(List<ChessGame> games) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(games);
    }

    public static <T> String jsonifyStats(T stats) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(stats);
    }

    public static Long convertIsoDateStringToEpochMs(String isoDate) throws ParseException {
        LocalDate date = LocalDate.parse(isoDate);
        LocalDateTime dateTime = date.atStartOfDay();
        Instant instant = dateTime.toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond() * 1000;
    }
}
