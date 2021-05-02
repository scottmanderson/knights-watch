package com.agincourt.chessstat.services;

import java.util.List;

public class PlayerStats {
    Long gameCount;
    Long winCount;
    Long lossCount;
    Long drawCount;

    List<Integer> ratingsHistory;
    List<Long> ratingsHistoryDates;

    PlayerStats() {}

    public Long getGameCount() {
        return gameCount;
    }

    public void setGameCount(Long gameCount) {
        this.gameCount = gameCount;
    }

    public Long getWinCount() {
        return winCount;
    }

    public void setWinCount(Long winCount) {
        this.winCount = winCount;
    }

    public Long getLossCount() {
        return lossCount;
    }

    public void setLossCount(Long lossCount) {
        this.lossCount = lossCount;
    }

    public Long getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Long drawCount) {
        this.drawCount = drawCount;
    }

    public List<Integer> getRatingsHistory() {
        return ratingsHistory;
    }

    public void setRatingsHistory(List<Integer> ratingsHistory) {
        this.ratingsHistory = ratingsHistory;
    }

    public List<Long> getRatingsHistoryDates() {
        return ratingsHistoryDates;
    }

    public void setRatingsHistoryDates(List<Long> ratingsHistoryDates) {
        this.ratingsHistoryDates = ratingsHistoryDates;
    }
}
