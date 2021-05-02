# Knight's Watch

## Knight's Watch is a data pipeline Using Apache Kafka and Spark to process games a chess player plays on Lichess.org and provide analytics on results by opening and time management.

## Components
This multi-module repo holds the components:

| Module                              | Tech                      | Function                                                                       |
| :---                                |         :----:            |        :---:                                                                   |
| chess-kafka-producer                | Kafka Producer            | Poll Lichess API and produce records to kafka stream                           |
| chess-kafka-spark-structured-stream | Spark                     | Transform chessgames adding scoring and extracing time information; push to DB |
| chess-stat                          | Spring Rest API           | Serve API calls to dashboard from data in DB                                   |
| chess-stat-dashboard                | React JS Frontend         | Displays information for the player                                            |

