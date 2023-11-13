package dev.dutta.abhijit.hashnode.constants

object SparkConstants {

  lazy val _SPARK_MASTER_LOCAL: String = "local[*]"
  lazy val _SPARK_CONFIG_KEY_SERIALIZER: String = "spark.serializer"
  lazy val _SPARK_CONFIG_VALUE_SERIALIZER: String = "org.apache.spark.serializer.KyroSerializer"
  lazy val _SPARK_CONFIG_KEY_DATETIME_MODE: String = "spark.sql.legacy.parquet.datetimeRebaseModeInWrite"
  lazy val _SPARK_CONFIG_VALUE_DATETIME_MODE: String = "LEGACY"
  lazy val _SPARK_CONFIG_KEY_AUTO_BROADCAST_THRESHOLD: String = "spark.sql.autoBroadcastJoinThreshold"

}
