package spark

import models.{ChessGameDbModel, ChessGameKafkaRecord}
import org.apache.spark.sql.functions.{col, udf, when}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import utils.ConfigManager.UserConfig

import scala.util.matching.Regex

trait TransformationLogic {

  val playerHandle: String = UserConfig.getValue("lichess.handle")

  def convertClockTimeToSeconds(clockTime: String): Int = {
    val digitExtractPattern: Regex = """(\d+):(\d+):(\d+)""".r
    val seconds: Int = digitExtractPattern.findFirstMatchIn(clockTime).map(_.group(1)).getOrElse(0).toString.toInt * 60 * 60 +
      digitExtractPattern.findFirstMatchIn(clockTime).map(_.group(2)).getOrElse(0).toString.toInt * 60 +
      digitExtractPattern.findFirstMatchIn(clockTime).map(_.group(3)).getOrElse(0).toString.toInt
    seconds
  }

  private val parseTimeUDF = udf((pgn: String, whitePlayerId: String) => {
    val clkPattern: Regex = """\{ \[%clk (\d+:\d+:\d+)] }""".r

    val groupClockRecords =
      clkPattern.findAllMatchIn(pgn).map(_.group(1)).map(convertClockTimeToSeconds).toArray

    val whiteTimeHistory =
      for (clock <- 0 until groupClockRecords.length by 2)
        yield groupClockRecords(clock)
    val blackTimeHistory =
      for (clock <- 1 until groupClockRecords.length by 2)
        yield groupClockRecords(clock)

    val whiteTimeSurplusHistory =
      for (clock <- blackTimeHistory.indices) // Use blackTimeHistory to avoid pulling nonexistent black move on games where white moved last
        yield Some(whiteTimeHistory(clock)).getOrElse(0) - Some(blackTimeHistory(clock)).getOrElse(0)

    val blackTimeSurplusHistory =
      for (clock <- blackTimeHistory.indices)
        yield Some(blackTimeHistory(clock)).getOrElse(0) - Some(whiteTimeHistory(clock)).getOrElse(0)



    val clockFields: Map[String, String] = Map(
      "whiteTimeRemaining" -> (if (whiteTimeHistory.nonEmpty)
                                 whiteTimeHistory.last.toString
                               else ""),
      "blackTimeRemaining" -> (if (blackTimeHistory.nonEmpty)
                                 blackTimeHistory.last.toString
                               else ""),
      "whiteTimeHistory" -> whiteTimeHistory.mkString(","),
      "blackTimeHistory" -> blackTimeHistory.mkString(","),
      "whiteTimeSurplusHistory" -> whiteTimeSurplusHistory.mkString(","),  // keeping Time Surplus Histories by color as they may be useful to determine if there is a general time management bias
      "blackTimeSurplusHistory" -> blackTimeSurplusHistory.mkString(","),
      "playerTimeSurplusHistory" -> (if (whitePlayerId == playerHandle) whiteTimeSurplusHistory.mkString(",") else blackTimeSurplusHistory.mkString(",")),
      "opponentTimeSurplusHistory" -> (if (whitePlayerId == playerHandle) blackTimeSurplusHistory.mkString(",") else whiteTimeSurplusHistory.mkString(",")),
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
        .withColumn("timeOutput", parseTimeUDF(col("pgn"), col("whitePlayerId")))
        .select(
          col("*"),
          col("timeOutput")
            .getItem("whiteTimeRemaining")
            .as("whiteTimeRemaining"),
          col("timeOutput")
            .getItem("blackTimeRemaining")
            .as("blackTimeRemaining"),
          col("timeOutput").getItem("whiteTimeHistory").as("whiteTimeHistory"),
          col("timeOutput").getItem("blackTimeHistory").as("blackTimeHistory"),
          col("timeOutput")
            .getItem("whiteTimeSurplusHistory").as("whiteTimeSurplusHistory"),
          col("timeOutput")
            .getItem("blackTimeSurplusHistory").as("blackTimeSurplusHistory"),
          col("timeOutput")
            .getItem("playerTimeSurplusHistory").as("playerTimeSurplusHistory"),
          col("timeOutput")
            .getItem("opponentTimeSurplusHistory").as("opponentTimeSurplusHistory")
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
        .withColumn("playerTimeEndingSurplus", col("playerTimeRemaining") - col("opponentTimeRemaining"))
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
      .withColumnRenamed("playerTimeEndingSurplus", "player_time_ending_surplus")
      .withColumnRenamed("whiteTimeSurplusHistory", "white_time_surplus_history")
      .withColumnRenamed("blackTimeSurplusHistory", "black_time_surplus_history")
      .withColumnRenamed("playerTimeSurplusHistory", "player_time_surplus_history")
      .withColumnRenamed("opponentTimeSurplusHistory", "opponent_time_surplus_history")
  }
}
