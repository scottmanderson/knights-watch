import Dependencies._

name := "chess-kafka-spark-structured-stream"

version := "0.1"

scalaVersion := "2.12.13"

libraryDependencies ++= compiledDependencies(
  sparkSqlKafka,
  sparkCore,
  sparkSql,
  postgreSql,
  typeSafeConfig
) ++ testDependencies(
  scalaTest
)
