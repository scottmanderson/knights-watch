package com.agincourt.chessstat.controller;

import com.agincourt.chessstat.services.ChessGameStatisticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class ChessGameStatisticsController {

    @Autowired
    private ChessGameStatisticsService chessGameStatisticsService;

    @GetMapping(value = "/statistics", produces = "application/json")
    String providePlayerStatistics(@RequestParam(value="after", required = false) String isoDate) throws JsonProcessingException, ParseException {
        if (isoDate==null) {
            return chessGameStatisticsService.playerStats();
        } else {
            return chessGameStatisticsService.playerStats(isoDate);
        }
    }
}
