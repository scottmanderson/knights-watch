package app

import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import models.{ChessGameDbModel, ChessGameKafkaRecord}
import spark.Job

object PushToPostgresql extends Job {

  override def run(spark: SparkSession): Unit = {
    val chessGamesDataset: Dataset[ChessGameKafkaRecord] = readFromKafka(spark)

    // Make transformations below before write to DB
    val chessGamesTransformed: DataFrame =
      runTransformations(chessGamesDataset, spark)
    // DB Load Logic
    val chessGamesDatasetDbModel =
      runColumnConversions(chessGamesTransformed)

    chessGamesDatasetDbModel.printSchema()

    writeToPostgresql(chessGamesDatasetDbModel)
  }
}
