package com.singularity.spark.core

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

/**
 * Simple Spark application demonstrating basic functionality
 */
object SimpleSparkApp extends SparkApp {
  
  override def run(): Unit = {
    println("=== Simple Spark Application ===")
    
    // Create some sample data
    val data = Seq(
      ("Alice", 25, "Engineer"),
      ("Bob", 30, "Manager"),
      ("Charlie", 35, "Analyst"),
      ("Diana", 28, "Engineer"),
      ("Eve", 32, "Manager")
    )
    
    // Create DataFrame
    val df = spark.createDataFrame(data).toDF("name", "age", "role")
    
    println("Original data:")
    df.show()
    
    // Perform some transformations
    val avgAgeByRole = df
      .groupBy("role")
      .agg(avg("age").as("avg_age"), count("*").as("count"))
      .orderBy("role")
    
    println("Average age by role:")
    avgAgeByRole.show()
    
    // Filter engineers
    val engineers = df.filter(col("role") === "Engineer")
    println("Engineers:")
    engineers.show()
    
    println("Spark application completed successfully!")
  }
}
