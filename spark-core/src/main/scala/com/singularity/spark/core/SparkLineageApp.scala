package com.singularity.spark.core

import java.util.concurrent.atomic.AtomicBoolean

import org.apache.spark.SparkContext

trait SparkLineageApp extends SparkApp {
  @transient lazy val enableLineageTracking = config.getBoolean("singularity.spark-cluster-info.enable-lineage-tracking")

  def run(args: Array[String]): Unit

  override def main(args: Array[String]): Unit = {
    try {
      run(args)
    } finally {
      spark.stop()
    }
  }
}