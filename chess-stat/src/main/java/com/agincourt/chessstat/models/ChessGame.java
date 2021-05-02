package com.agincourt.chessstat.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="chessgames")
public class ChessGame implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lichessId;
    private Boolean rated;
    private String variant;
    private String speed;
    private String perf;
    private Long createdAt;
    private Long lastMoveAt;
    private String status;
    private String whitePlayerId;
    private Integer whitePlayerRating;
    private Integer whitePlayerRatingDiff;
    private String blackPlayerId;
    private Integer blackPlayerRating;
    private Integer blackPlayerRatingDiff;
    private String winner;
    private String openingEco;
    @Column(columnDefinition = "text")
    private String openingName;
    private Integer openingPly;
    @Column(columnDefinition = "text")
    private String moves;
    @Column(columnDefinition = "text")
    private String pgn;
    private Integer clockInitial;
    private Integer clockIncrement;
    private Integer clockTotalTime;
    private String whiteTimeRemaining;
    private String blackTimeRemaining;
    @Column(columnDefinition = "text")
    private String whiteTimeHistory;
    @Column(columnDefinition = "text")
    private String blackTimeHistory;
    private Double playerScore;
    private String playerColor;
    private Integer playerRating;
    private Integer playerRatingDiff;
    private String playerTimeRemaining;
    private String opponentTimeRemaining;


    public ChessGame() {
    }

    public ChessGame(String lichessId, Boolean rated, String variant, String speed, String perf, Long createdAt,
                     Long lastMoveAt, String status, String whitePlayerId, Integer whitePlayerRating,
                     Integer whitePlayerRatingDiff, String blackPlayerId, Integer blackPlayerRating,
                     Integer blackPlayerRatingDiff, String winner, String openingEco, String openingName,
                     Integer openingPly, String moves, String pgn, Integer clockInitial, Integer clockIncrement,
                     Integer clockTotalTime, String whiteTimeRemaining, String blackTimeRemaining, String whiteTimeHistory,
                     String blackTimeHistory, Double playerScore, String playerColor, Integer playerRating,
                     Integer playerRatingDiff, String playerTimeRemaining, String opponentTimeRemaining) {
        this.lichessId = lichessId;
        this.rated = rated;
        this.variant = variant;
        this.speed = speed;
        this.perf = perf;
        this.createdAt = createdAt;
        this.lastMoveAt = lastMoveAt;
        this.status = status;
        this.whitePlayerId = whitePlayerId;
        this.whitePlayerRating = whitePlayerRating;
        this.whitePlayerRatingDiff = whitePlayerRatingDiff;
        this.blackPlayerId = blackPlayerId;
        this.blackPlayerRating = blackPlayerRating;
        this.blackPlayerRatingDiff = blackPlayerRatingDiff;
        this.winner = winner;
        this.openingEco = openingEco;
        this.openingName = openingName;
        this.openingPly = openingPly;
        this.moves = moves;
        this.pgn = pgn;
        this.clockInitial = clockInitial;
        this.clockIncrement = clockIncrement;
        this.clockTotalTime = clockTotalTime;
        this.whiteTimeRemaining = whiteTimeRemaining;
        this.blackTimeRemaining = blackTimeRemaining;
        this.whiteTimeHistory = whiteTimeHistory;
        this.blackTimeHistory = blackTimeHistory;
        this.playerScore = playerScore;
        this.playerColor = playerColor;
        this.playerRating = playerRating;
        this.playerRatingDiff = playerRatingDiff;
        this.playerTimeRemaining = playerTimeRemaining;
        this.opponentTimeRemaining = opponentTimeRemaining;
    }

    public Long getId() {
        return id;
    }

    public String getLichessId() {
        return lichessId;
    }

    public Boolean getRated() {
        return rated;
    }

    public String getVariant() {
        return variant;
    }

    public String getSpeed() {
        return speed;
    }

    public String getPerf() {
        return perf;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getLastMoveAt() {
        return lastMoveAt;
    }

    public String getStatus() {
        return status;
    }

    public String getWhitePlayerId() {
        return whitePlayerId;
    }

    public Integer getWhitePlayerRating() {
        return whitePlayerRating;
    }

    public Integer getWhitePlayerRatingDiff() {
        return whitePlayerRatingDiff;
    }

    public String getBlackPlayerId() {
        return blackPlayerId;
    }

    public Integer getBlackPlayerRating() {
        return blackPlayerRating;
    }

    public Integer getBlackPlayerRatingDiff() {
        return blackPlayerRatingDiff;
    }

    public String getWinner() {
        return winner;
    }

    public String getOpeningEco() {
        return openingEco;
    }

    public String getOpeningName() {
        return openingName;
    }

    public Integer getOpeningPly() {
        return openingPly;
    }

    public String getMoves() {
        return moves;
    }

    public String getPgn() {
        return pgn;
    }

    public Integer getClockInitial() {
        return clockInitial;
    }

    public Integer getClockIncrement() {
        return clockIncrement;
    }

    public Integer getClockTotalTime() {
        return clockTotalTime;
    }

    public String getWhiteTimeRemaining() {
        return whiteTimeRemaining;
    }

    public String getBlackTimeRemaining() {
        return blackTimeRemaining;
    }

    public String getWhiteTimeHistory() {
        return whiteTimeHistory;
    }

    public String getBlackTimeHistory() {
        return blackTimeHistory;
    }

    public Double getPlayerScore() {
        return playerScore;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public Integer getPlayerRating() {
        return playerRating;
    }

    public Integer getPlayerRatingDiff() {
        return playerRatingDiff;
    }

    public String getPlayerTimeRemaining() {
        return playerTimeRemaining;
    }

    public String getOpponentTimeRemaining() {
        return opponentTimeRemaining;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLichessId(String lichessId) {
        this.lichessId = lichessId;
    }

    public void setRated(Boolean rated) {
        this.rated = rated;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setPerf(String perf) {
        this.perf = perf;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setLastMoveAt(Long lastMoveAt) {
        this.lastMoveAt = lastMoveAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWhitePlayerId(String whitePlayerId) {
        this.whitePlayerId = whitePlayerId;
    }

    public void setWhitePlayerRating(Integer whitePlayerRating) {
        this.whitePlayerRating = whitePlayerRating;
    }

    public void setWhitePlayerRatingDiff(Integer whitePlayerRatingDiff) {
        this.whitePlayerRatingDiff = whitePlayerRatingDiff;
    }

    public void setBlackPlayerId(String blackPlayerId) {
        this.blackPlayerId = blackPlayerId;
    }

    public void setBlackPlayerRating(Integer blackPlayerRating) {
        this.blackPlayerRating = blackPlayerRating;
    }

    public void setBlackPlayerRatingDiff(Integer blackPlayerRatingDiff) {
        this.blackPlayerRatingDiff = blackPlayerRatingDiff;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setOpeningEco(String openingEco) {
        this.openingEco = openingEco;
    }

    public void setOpeningName(String openingName) {
        this.openingName = openingName;
    }

    public void setOpeningPly(Integer openingPly) {
        this.openingPly = openingPly;
    }

    public void setMoves(String moves) {
        this.moves = moves;
    }

    public void setPgn(String pgn) {
        this.pgn = pgn;
    }

    public void setClockInitial(Integer clockInitial) {
        this.clockInitial = clockInitial;
    }

    public void setClockIncrement(Integer clockIncrement) {
        this.clockIncrement = clockIncrement;
    }

    public void setClockTotalTime(Integer clockTotalTime) {
        this.clockTotalTime = clockTotalTime;
    }

    public void setWhiteTimeRemaining(String whiteTimeRemaining) {
        this.whiteTimeRemaining = whiteTimeRemaining;
    }

    public void setBlackTimeRemaining(String blackTimeRemaining) {
        this.blackTimeRemaining = blackTimeRemaining;
    }

    public void setWhiteTimeHistory(String whiteTimeHistory) {
        this.whiteTimeHistory = whiteTimeHistory;
    }

    public void setBlackTimeHistory(String blackTimeHistory) {
        this.blackTimeHistory = blackTimeHistory;
    }

    public void setPlayerScore(Double playerScore) {
        this.playerScore = playerScore;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public void setPlayerRating(Integer playerRating) {
        this.playerRating = playerRating;
    }

    public void setPlayerRatingDiff(Integer playerRatingDiff) {
        this.playerRatingDiff = playerRatingDiff;
    }

    public void setPlayerTimeRemaining(String playerTimeRemaining) {
        this.playerTimeRemaining = playerTimeRemaining;
    }

    public void setOpponentTimeRemaining(String opponentTimeRemaining) {
        this.opponentTimeRemaining = opponentTimeRemaining;
    }


}
