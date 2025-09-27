package com.singularity.spark.core

import com.typesafe.config.Config
import org.apache.spark.sql.{Dataset, Row, SparkSession}

object SparkApp {
  @transient private lazy val config: Config = {
    import com.typesafe.config.ConfigFactory
    ConfigFactory.load()
  }
  
  def getConfig: Config = config
}

abstract class SparkApp extends Serializable {
  
  @transient protected lazy val spark: SparkSession = {
    try {
      SparkSession.builder()
        .appName(getClass.getSimpleName)
        .master("local[*]")
        .config("spark.sql.adaptive.enabled", "true")
        .config("spark.sql.adaptive.coalescePartitions.enabled", "true")
        .getOrCreate()
    } catch {
      case e: Exception =>
        println(s"Failed to create SparkSession: ${e.getMessage}")
        throw e
    }
  }
  
  @transient protected lazy val config: Config = SparkApp.getConfig
  
  def run(): Unit
  
  def main(args: Array[String]): Unit = {
    try {
      run()
    } finally {
      spark.stop()
    }
  }
}