package com.agincourt.chessstat.services;

import com.agincourt.chessstat.repository.ChessGameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;

import static com.agincourt.chessstat.util.Formatting.convertIsoDateStringToEpochMs;
import static com.agincourt.chessstat.util.Formatting.jsonifyGames;

public interface ChessGameRetrievalService extends ChessGameRepository {
    default String retrieveAllGames() throws JsonProcessingException {
        return jsonifyGames(findAllByOrderByLastMoveAtAsc());
    }

    default String retrieveWhiteGames() throws JsonProcessingException {
        return jsonifyGames(findByPlayerColorIsOrderByLastMoveAtAsc("white"));
    }

    default String retrieveBlackGames() throws JsonProcessingException {
        return jsonifyGames(findByPlayerColorIsOrderByLastMoveAtAsc("black"));
    }

    default String retrieveAllGamesAfter(String isoDate) throws JsonProcessingException, ParseException {
        Long epochMsTimestamp = convertIsoDateStringToEpochMs(isoDate);
        return jsonifyGames(findByLastMoveAtAfterOrderByLastMoveAtAsc(epochMsTimestamp));
    }

    default String retrieveAllWhiteGamesAfter(String isoDate) throws JsonProcessingException, ParseException {
        Long epochMsTimestamp = convertIsoDateStringToEpochMs(isoDate);
        return jsonifyGames(findByPlayerColorIsAndLastMoveAtOrderByLastMoveAtAsc("white", epochMsTimestamp));
    }

    default String retrieveAllBlackGamesAfter(String isoDate) throws JsonProcessingException, ParseException {
        Long epochMsTimestamp = convertIsoDateStringToEpochMs(isoDate);
        return jsonifyGames(findByPlayerColorIsAndLastMoveAtOrderByLastMoveAtAsc("black", epochMsTimestamp));
    }
}
