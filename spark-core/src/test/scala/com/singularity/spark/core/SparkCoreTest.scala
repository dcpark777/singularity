package com.singularity.spark.core

import org.scalatest.funsuite.AnyFunSuite
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import com.holdenkarau.spark.testing.{DataFrameSuiteBase, SharedSparkContext}
import org.apache.log4j.{Level, Logger}

class SparkCoreTest extends AnyFunSuite with DataFrameSuiteBase with SharedSparkContext {
  
  // Configure quieter logging for tests
  override def beforeAll(): Unit = {
    super.beforeAll()
    
    // Set log levels to reduce noise during testing
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hadoop").setLevel(Level.WARN)
    Logger.getLogger("org.apache.hive").setLevel(Level.WARN)
    Logger.getLogger("org.apache.parquet").setLevel(Level.WARN)
    Logger.getLogger("org.apache.avro").setLevel(Level.WARN)
    Logger.getLogger("org.apache.kafka").setLevel(Level.WARN)
    Logger.getLogger("org.apache.zookeeper").setLevel(Level.WARN)
    Logger.getLogger("org.apache.curator").setLevel(Level.WARN)
    Logger.getLogger("org.apache.http").setLevel(Level.WARN)
    Logger.getLogger("org.apache.commons").setLevel(Level.WARN)
    Logger.getLogger("com.fasterxml.jackson").setLevel(Level.WARN)
    Logger.getLogger("io.netty").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)
    Logger.getLogger("com.typesafe").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty").setLevel(Level.WARN)
    Logger.getLogger("org.apache.catalina").setLevel(Level.WARN)
    Logger.getLogger("org.apache.tomcat").setLevel(Level.WARN)
    Logger.getLogger("org.apache.logging").setLevel(Level.WARN)
    Logger.getLogger("org.slf4j").setLevel(Level.WARN)
    Logger.getLogger("ch.qos.logback").setLevel(Level.WARN)
    
    // Keep our application logs visible
    Logger.getLogger("com.singularity").setLevel(Level.INFO)
    
    println("Spark test environment configured with quiet logging")
  }
  
  test("Spark session can be created") {
    // Test basic Spark functionality
    val data = Seq(1, 2, 3, 4, 5)
    val rdd = sc.parallelize(data)
    val result = rdd.map(_ * 2).collect()
    
    assert(result.sameElements(Array(2, 4, 6, 8, 10)))
  }
  
  test("Spark SQL works") {
    import spark.implicits._
    
    val data = Seq(("Alice", 25), ("Bob", 30))
    val df = data.toDF("name", "age")
    
    val result = df.filter($"age" > 25).collect()
    assert(result.length == 1)
    assert(result(0).getString(0) == "Bob")
  }
  
  test("DataFrame operations with spark-testing-base") {
    import spark.implicits._
    
    val input1 = sc.parallelize(Seq(1, 2, 3, 4, 5)).toDF("value")
    val input2 = sc.parallelize(Seq(2, 4, 6, 8, 10)).toDF("value")
    
    val result1 = input1.filter($"value" > 3)
    val result2 = input2.filter($"value" < 8)
    
    val expected1 = sc.parallelize(Seq(4, 5)).toDF("value")
    val expected2 = sc.parallelize(Seq(2, 4, 6)).toDF("value")
    
    // Use spark-testing-base assertion
    assertDataFrameEquals(result1, expected1)
    assertDataFrameEquals(result2, expected2)
  }
  
  test("DataFrame join operations") {
    import spark.implicits._
    
    val users = sc.parallelize(Seq(
      (1, "Alice"), 
      (2, "Bob"), 
      (3, "Charlie")
    )).toDF("id", "name")
    
    val orders = sc.parallelize(Seq(
      (1, 100), 
      (2, 200), 
      (1, 150)
    )).toDF("user_id", "amount")
    
    val joined = users.join(orders, users("id") === orders("user_id"))
    val result = joined.select("name", "amount").collect()
    
    assert(result.length == 3)
    assert(result.map(_.getString(0)).toSet == Set("Alice", "Bob"))
  }
  
  test("DataFrame operations with floating point numbers") {
    import spark.implicits._
    
    val input = sc.parallelize(Seq(
      (1.0, 2.5),
      (3.0, 4.7),
      (5.0, 6.9)
    )).toDF("x", "y")
    
    val result = input.filter($"x" > 2.0)
    val expected = sc.parallelize(Seq(
      (3.0, 4.7),
      (5.0, 6.9)
    )).toDF("x", "y")
    
    // Use spark-testing-base assertion
    assertDataFrameEquals(result, expected)
  }
  
  test("Test configuration and logging setup") {
    // Verify that our test configuration is working
    assert(spark.conf.get("spark.app.name") == "test")
    assert(spark.conf.get("spark.master") == "local[*]")
    
    // Test that we can read our test configuration
    val config = com.typesafe.config.ConfigFactory.load("application-test.conf")
    assert(config.getString("app.name") == "singularity-spark-test")
    assert(config.getString("app.environment") == "test")
    
    println("✓ Test configuration verified successfully")
  }
}
