package dev.dutta.abhijit.hashnode

import dev.dutta.abhijit.hashnode.constants.IntConstants._
import dev.dutta.abhijit.hashnode.constants.SparkConstants._
import dev.dutta.abhijit.hashnode.constants.StringConstants._
import org.apache.spark.sql.SparkSession

trait Session {
  lazy val spark: SparkSession = SparkSession
    .builder()
    .appName(_TEST_APP_NAME)
    .master(_SPARK_MASTER_LOCAL)
    .config(_SPARK_CONFIG_KEY_SERIALIZER, _SPARK_CONFIG_VALUE_SERIALIZER)
    .config(_SPARK_CONFIG_KEY_DATETIME_MODE, _SPARK_CONFIG_VALUE_DATETIME_MODE)
    .config(_SPARK_CONFIG_KEY_AUTO_BROADCAST_THRESHOLD, _250_MILLION)
    .getOrCreate()

  spark.sparkContext.setLogLevel(_ERROR)

}
