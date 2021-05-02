package utils

object Constants {
  val APP_NAME = "chess-kafka-structured-stream"
  val SPARK_MASTER = "local[2]"
  val CHECKPOINT_PATH = "src/main/resources/checkpoints"
  val ERROR_LOG_LEVEL = "ERROR"

  val BROKER = "localhost:9092"
  val TOPIC = "chessgames"
  val GROUP_ID = "main"
  val EARLIEST = true
  val KAFKA_SOURCE = "kafka"

  val POSTGRESQL_FORMAT = "jdbc"
  val POSTGRESQL_DRIVER = "org.postgresql.Driver"
  val POSTGRESQL_URL = "jdbc:postgresql://localhost:5432/chessgames"
}
