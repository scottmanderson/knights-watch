package services

import models.ChessGameKafkaRecord
import models.ChessGameKafkaRecord._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{Dataset, SparkSession}
import utils.ConfigManager.KafkaConfig
import utils.Constants._
import utils.Logging

trait KafkaService extends Logging {

  val brokers: String = KafkaConfig.getValue("broker")
  val sourceTopic: String = KafkaConfig.getValue("source.topic")
  val groupId: String = KafkaConfig.getValue("source.group.id")
  val fromBeginning: String =
    if (KafkaConfig.getBoolean("source.earliest")) "earliest" else "latest"

  /**
    * Reads dataframe from kafka as dataset of Bands
    *
    * @param spark SparkSession
    * @return Dataset of ChessGameKafkaRecords
    */
  def readFromKafka(spark: SparkSession): Dataset[ChessGameKafkaRecord] = {
    info(s"Reading from kafka with topic : ${sourceTopic}")
    import spark.implicits._
    spark.readStream
      .format(KAFKA_SOURCE)
      .options(kafkaSourceOptions)
      .load()
      .selectExpr("cast(value as string) as value")
      .select(
        from_json(col("value"), chessGameKafkaRecordSchema)
          .as[ChessGameKafkaRecord]
      )
  }

  /**
    * Kafka consumer configuration properties
    *
    * @return Map of String -> String
    */
  def kafkaSourceOptions: Map[String, String] =
    Map(
      ("kafka.bootstrap.servers", brokers),
      ("group.id", groupId),
      ("startingOffsets", fromBeginning),
      ("subscribe", sourceTopic)
    )
}
