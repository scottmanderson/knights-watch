package com.agincourt.chessstat.services;

import com.agincourt.chessstat.models.ChessGame;
import com.agincourt.chessstat.repository.ChessGameRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import static com.agincourt.chessstat.util.Formatting.convertIsoDateStringToEpochMs;
import static com.agincourt.chessstat.util.Formatting.jsonifyStats;

public interface ChessGameStatisticsService extends ChessGameRepository {

    default String playerStats() throws JsonProcessingException {
        List<ChessGame> games = findAllByOrderByLastMoveAtAsc();
        return playerStatCalculation(games);
    }

    default String playerStats(String isoDate) throws JsonProcessingException, ParseException {
        Long epochMsTimestamp = convertIsoDateStringToEpochMs(isoDate);
        List<ChessGame> games = findByLastMoveAtAfterOrderByLastMoveAtAsc(epochMsTimestamp);
        return playerStatCalculation(games);
    }

    default String playerStatCalculation(List<ChessGame> games) throws JsonProcessingException {
        PlayerStats stats = new PlayerStats();
        stats.gameCount = (long) games.size();
        stats.winCount = games.stream().filter(g -> g.getPlayerScore() == 1).count();
        stats.lossCount = games.stream().filter(g -> g.getPlayerScore() == 0).count();
        stats.drawCount = games.stream().filter(g -> g.getPlayerScore() == 0.5).count();
        stats.ratingsHistory = games.stream().map(ChessGame::getPlayerRating).collect(Collectors.toList());
        stats.ratingsHistoryDates = games.stream().map(ChessGame::getLastMoveAt).collect(Collectors.toList());
        stats.playerTimeEndingSurpluses = games.stream()
                .map(ChessGame::getPlayerTimeEndingSurplus).collect(Collectors.toList());
        stats.playerRatingDiff = games.stream().map(ChessGame::getPlayerRatingDiff).collect(Collectors.toList());
        stats.scores = games.stream().map(ChessGame::getPlayerScore).collect(Collectors.toList());

        return jsonifyStats(stats);
    }
}
