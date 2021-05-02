package app

import org.apache.spark.sql.{Dataset, SparkSession}
import models.ChessGameKafkaRecord
import spark.Job

object CommandLineOutput extends Job {

  override def run(spark: SparkSession): Unit = {
    val chessGamesDataset: Dataset[ChessGameKafkaRecord] = readFromKafka(spark)

    // Make transformations below before write to DB
    val chessGamesTransform =
      runTransformations(chessGamesDataset, spark)
    chessGamesTransform.printSchema()

    // Output below

    val q = chessGamesTransform.writeStream
      .format("console")
      .outputMode("append")
      .start()
    Thread.sleep(10000)
    q.stop()

  }

}
