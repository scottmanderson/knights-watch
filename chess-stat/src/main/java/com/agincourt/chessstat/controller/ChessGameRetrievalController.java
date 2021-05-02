package com.agincourt.chessstat.controller;

import com.agincourt.chessstat.exception.ResourceNotFoundException;
import com.agincourt.chessstat.models.ChessGame;
import com.agincourt.chessstat.services.ChessGameRetrievalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class ChessGameRetrievalController {

    @Autowired
    private ChessGameRetrievalService chessGameRetrievalService;

    @GetMapping(value = "/games", produces = "application/json")
    String provideChessGamesAfter(@RequestParam(value = "after", required = false) String isoDate) throws JsonProcessingException, ParseException {
        if (isoDate == null) {
            return chessGameRetrievalService.retrieveAllGames();
        } else {
            return chessGameRetrievalService.retrieveAllGamesAfter(isoDate);
        }
    }

    @GetMapping(value = "/games/white", produces = "application/json")
    String provideWhiteChessGamesAfter(@RequestParam(value = "after", required = false) String isoDate) throws JsonProcessingException, ParseException {
        if (isoDate == null) {
            return chessGameRetrievalService.retrieveWhiteGames();
        } else {
            return chessGameRetrievalService.retrieveAllWhiteGamesAfter(isoDate);
        }
    }

    @GetMapping(value = "/games/black", produces = "application/json")
    String provideBlackChessGamesAfter(@RequestParam(value = "after", required = false) String isoDate) throws JsonProcessingException, ParseException {
        if (isoDate == null) {
            return chessGameRetrievalService.retrieveBlackGames();
        } else {
            return chessGameRetrievalService.retrieveAllBlackGamesAfter(isoDate);
        }
    }

}
