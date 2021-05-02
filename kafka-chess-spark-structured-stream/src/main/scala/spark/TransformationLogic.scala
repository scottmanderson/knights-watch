package spark

import models.{ChessGameDbModel, ChessGameKafkaRecord}
import org.apache.spark.sql.functions.{col, udf, when}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import utils.ConfigManager.UserConfig

import scala.util.matching.Regex

trait TransformationLogic {

  val playerHandle: String = UserConfig.getValue("lichess.handle")

  private val parseTimeUDF = udf((pgn: String) => {
    val clkPattern: Regex = """\{ \[%clk (\d+:\d+:\d+)] }""".r

    val groupClockRecords =
      clkPattern.findAllMatchIn(pgn).map(_.group(1)).toArray

    val whiteTimeHistory =
      for (clock <- 0 until groupClockRecords.length by 2)
        yield groupClockRecords(clock)
    val blackTimeHistory =
      for (clock <- 1 until groupClockRecords.length by 2)
        yield groupClockRecords(clock)

    val clockFields: Map[String, String] = Map(
      "whiteTimeRemaining" -> (if (whiteTimeHistory.nonEmpty)
                                 whiteTimeHistory.last
                               else ""),
      "blackTimeRemaining" -> (if (blackTimeHistory.nonEmpty)
                                 blackTimeHistory.last
                               else ""),
      "whiteTimeHistory" -> whiteTimeHistory.mkString(","),
      "blackTimeHistory" -> blackTimeHistory.mkString(",")
    )
    clockFields
  })

  def runTransformations(
      chessGamesDataset: Dataset[ChessGameKafkaRecord],
      spark: SparkSession
  ): DataFrame = {

    // Make transformations below before write to DB
    val chessGamesTransform: DataFrame =
      chessGamesDataset
        .withColumn("timeOutput", parseTimeUDF(col("pgn")))
        .select(
          col("*"),
          col("timeOutput")
            .getItem("whiteTimeRemaining")
            .as("whiteTimeRemaining"),
          col("timeOutput")
            .getItem("blackTimeRemaining")
            .as("blackTimeRemaining"),
          col("timeOutput").getItem("whiteTimeHistory").as("whiteTimeHistory"),
          col("timeOutput").getItem("blackTimeHistory").as("blackTimeHistory")
        )
        .drop(col("timeOutput"))
        .withColumn(
          "playerScore",
          when(
            col("whitePlayerId") === playerHandle && col("winner") === "white",
            1
          ).when(
              col("whitePlayerId") === playerHandle && col(
                "winner"
              ) === "black",
              0
            )
            .when(
              col("blackPlayerId") === playerHandle && col(
                "winner"
              ) === "black",
              1
            )
            .when(
              col("blackPlayerId") === playerHandle && col(
                "winner"
              ) === "white",
              0
            )
            .otherwise(0.5)
        )
        .withColumn(
          "playerColor",
          when(col("whitePlayerId") === playerHandle, "white")
            .when(col("blackPlayerId") === playerHandle, "black")
            .otherwise(null)
        )
        .withColumn(
          "playerRating",
          when(col("whitePlayerId") === playerHandle, col("whitePlayerRating"))
            .when(
              col("blackPlayerId") === playerHandle,
              col("blackPlayerRating")
            )
            .otherwise(null)
        )
        .withColumn(
          "playerRatingDiff",
          when(
            col("whitePlayerId") === playerHandle,
            col("whitePlayerRatingDiff")
          ).when(
              col("blackPlayerId") === playerHandle,
              col("blackPlayerRatingDiff")
            )
            .otherwise(null)
        )
        .withColumn(
          "playerTimeRemaining",
          when(
            col("whitePlayerId") === playerHandle,
            col("whiteTimeRemaining")
          ).when(
              col("blackPlayerId") === playerHandle,
              col("blackTimeRemaining")
            )
            .otherwise(null)
        )
        .withColumn(
          "opponentTimeRemaining",
          when(
            col("whitePlayerId") =!= playerHandle,
            col("whiteTimeRemaining")
          ).when(
              col("blackPlayerId") =!= playerHandle,
              col("blackTimeRemaining")
            )
            .otherwise(null)
        )

    chessGamesTransform
  }

  def runColumnConversions(
      chessGamesDataFrame: DataFrame
  ): DataFrame = {
    chessGamesDataFrame
      .withColumnRenamed("lichessId", "lichess_id")
      .withColumnRenamed("createdAt", "created_at")
      .withColumnRenamed("lastMoveAt", "last_move_at")
      .withColumnRenamed("whitePlayerId", "white_player_id")
      .withColumnRenamed("whitePlayerRating", "white_player_rating")
      .withColumnRenamed("whitePlayerRatingDiff", "white_player_rating_diff")
      .withColumnRenamed("blackPlayerId", "black_player_id")
      .withColumnRenamed("blackPlayerRating", "black_player_rating")
      .withColumnRenamed("blackPlayerRatingDiff", "black_player_rating_diff")
      .withColumnRenamed("openingECO", "opening_eco")
      .withColumnRenamed("openingName", "opening_name")
      .withColumnRenamed("openingPly", "opening_ply")
      .withColumnRenamed("clockInitial", "clock_initial")
      .withColumnRenamed("clockIncrement", "clock_increment")
      .withColumnRenamed("clockTotalTime", "clock_total_time")
      .withColumnRenamed("whiteTimeRemaining", "white_time_remaining")
      .withColumnRenamed("blackTimeRemaining", "black_time_remaining")
      .withColumnRenamed("whiteTimeHistory", "white_time_history")
      .withColumnRenamed("blackTimeHistory", "black_time_history")
      .withColumnRenamed("playerScore", "player_score")
      .withColumnRenamed("playerColor", "player_color")
      .withColumnRenamed("playerRating", "player_rating")
      .withColumnRenamed("playerRatingDiff", "player_rating_diff")
      .withColumnRenamed("playerTimeRemaining", "player_time_remaining")
      .withColumnRenamed("opponentTimeRemaining", "opponent_time_remaining")
  }
}
