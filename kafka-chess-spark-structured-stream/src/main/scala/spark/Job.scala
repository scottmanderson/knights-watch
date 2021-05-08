package spark

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import services.{KafkaService, PostgresqlService}
import utils.ConfigManager.SparkConfig
import utils.Constants.{APP_NAME, ERROR_LOG_LEVEL, SPARK_MASTER}
import utils.Logging

trait Job
    extends Serializable
    with Logging
    with KafkaService
    with PostgresqlService
    with TransformationLogic {

  val master: String = SparkConfig.getValueOpt("master").getOrElse {
    noConfigFound("master"); SPARK_MASTER
  }
  val appName: String = SparkConfig.getValueOpt("app.name").getOrElse {
    noConfigFound("app.name"); APP_NAME
  }
  val logLevel: String = SparkConfig.getValueOpt("log.level").getOrElse {
    noConfigFound("log.level"); ERROR_LOG_LEVEL
  }

  def main(args: Array[String]): Unit = {
    run(createSparkSession())
  }

  def createSparkSession(
      config: Map[String, String] = Map.empty
  ): SparkSession = {
    val spark = SparkSession
      .builder()
      .config(new SparkConf().setAll(config))
      .appName(appName)
      .master(master)
      .getOrCreate()

    spark.sparkContext.setLogLevel(logLevel)
    spark
  }

  def run(spark: SparkSession): Unit
}
