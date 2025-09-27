import sbt._

object Dependencies {
  
  // Global version definitions
  object Versions {
    val scala = "3.3.1"
    val scalaTest = "3.2.17"
    val munit = "0.7.29"
    val spark = "4.0.0-preview2"
    val sparkScala213 = "4.0.0-preview2" // Spark version for Scala 2.13 projects
    val sparkTestingBase = "4.0.0-preview2_2.0.1" // Spark Testing Base version (supports Scala 2.13)
    val typesafeConfig = "1.4.3"
    val logback = "1.2.12" // Compatible with Java 8
    val slf4j = "1.7.36" // Compatible with Java 8
    val jackson = "2.17.2"
    val scalaLogging = "3.9.5"
    val akka = "2.8.5"
    val cats = "2.10.0"
    val circe = "0.14.6"
  }
  
  // Core dependencies (used by most projects)
  object Core {
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % Test
    val munit = "org.scalameta" %% "munit" % Versions.munit % Test
    val typesafeConfig = "com.typesafe" % "config" % Versions.typesafeConfig
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
    val logback = "ch.qos.logback" % "logback-classic" % Versions.logback
    val slf4j = "org.slf4j" % "slf4j-api" % Versions.slf4j
  }
  
  // Spark dependencies (Scala 2.13 compatible)
  object Spark {
    val core = "org.apache.spark" %% "spark-core" % Versions.sparkScala213
    val sql = "org.apache.spark" %% "spark-sql" % Versions.sparkScala213
    val streaming = "org.apache.spark" %% "spark-streaming" % Versions.sparkScala213
    val mllib = "org.apache.spark" %% "spark-mllib" % Versions.sparkScala213
    val graphx = "org.apache.spark" %% "spark-graphx" % Versions.sparkScala213
    
    // Spark test dependencies
    val testCore = "org.apache.spark" %% "spark-core" % Versions.sparkScala213 % Test classifier "tests"
    val testSql = "org.apache.spark" %% "spark-sql" % Versions.sparkScala213 % Test classifier "tests"
    val testingBase = "com.holdenkarau" %% "spark-testing-base" % Versions.sparkTestingBase % Test
  }
  
  // Functional programming dependencies
  object Functional {
    val cats = "org.typelevel" %% "cats-core" % Versions.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % "3.5.2"
    val circeCore = "io.circe" %% "circe-core" % Versions.circe
    val circeGeneric = "io.circe" %% "circe-generic" % Versions.circe
    val circeParser = "io.circe" %% "circe-parser" % Versions.circe
  }
  
  // Akka dependencies
  object Akka {
    val actor = "com.typesafe.akka" %% "akka-actor" % Versions.akka
    val stream = "com.typesafe.akka" %% "akka-stream" % Versions.akka
    val http = "com.typesafe.akka" %% "akka-http" % "10.5.2"
  }
  
  // Jackson dependencies
  object Jackson {
    val core = "com.fasterxml.jackson.core" % "jackson-core" % Versions.jackson
    val databind = "com.fasterxml.jackson.core" % "jackson-databind" % Versions.jackson
    val annotations = "com.fasterxml.jackson.core" % "jackson-annotations" % Versions.jackson
    val scala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % Versions.jackson
  }
  
  // Scala 2.13 specific dependencies
  object Core213 {
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Versions.scalaLogging
    val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest % Test
    val munit = "org.scalameta" %% "munit" % Versions.munit % Test
  }
  
  // Common dependency groups
  object Groups {
    val logging = Seq(Core.scalaLogging, Core.logback, Core.slf4j)
    val logging213 = Seq(Core213.scalaLogging, Core.logback, Core.slf4j)
    val testing = Seq(Core.scalaTest, Core.munit)
    val testing213 = Seq(Core213.scalaTest, Core213.munit)
    val sparkCore = Seq(Spark.core, Spark.sql)
    val sparkFull = Seq(Spark.core, Spark.sql, Spark.streaming, Spark.mllib, Spark.graphx)
    val sparkTesting = Seq(Spark.testingBase)
    val functional = Seq(Functional.cats, Functional.catsEffect)
    val circe = Seq(Functional.circeCore, Functional.circeGeneric, Functional.circeParser)
    val jackson = Seq(Jackson.core, Jackson.databind, Jackson.annotations, Jackson.scala)
  }
}
