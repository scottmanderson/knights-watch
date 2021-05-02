package models

import org.apache.spark.sql.Encoders
import org.apache.spark.sql.types.StructType

case class ChessGameKafkaRecord(
    lichessId: String,
    rated: Option[Boolean],
    variant: String,
    speed: String,
    perf: String,
    createdAt: Option[Long],
    lastMoveAt: Option[Long],
    status: String,
    whitePlayerId: String,
    whitePlayerRating: Option[Long],
    whitePlayerRatingDiff: Option[Long],
    blackPlayerId: String,
    blackPlayerRating: Option[Long],
    blackPlayerRatingDiff: Option[Long],
    winner: String,
    openingECO: String,
    openingName: String,
    openingPly: Option[Long],
    moves: String,
    pgn: String,
    clockInitial: Option[Long],
    clockIncrement: Option[Long],
    clockTotalTime: Option[Long]
)

object ChessGameKafkaRecord {
  val chessGameKafkaRecordSchema: StructType =
    Encoders.product[ChessGameDbModel].schema
}
