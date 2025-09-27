import sbt._
import Keys._
import Dependencies._
import sbt.util.Level

object Projects {
  
  // Common settings for all projects
  val commonSettings = Seq(
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature", 
      "-unchecked",
      "-Xfatal-warnings"
    ),
    Test / parallelExecution := false
  )
  
  // Root project
  lazy val root = (project in file("."))
    .settings(
      name := "singularity",
      publish / skip := true
    )
    .aggregate(core, utils, examples, `spark-core`, analytics)
  
  // Core project
  lazy val core = (project in file("core"))
    .settings(
      commonSettings,
      name := "core",
      libraryDependencies ++= Groups.testing ++ Groups.logging
    )
  
  // Utils project
  lazy val utils = (project in file("utils"))
    .settings(
      commonSettings,
      name := "utils",
      libraryDependencies ++= Groups.testing ++ Groups.logging ++ Seq(Core.typesafeConfig)
    )
    .dependsOn(core)
  
  // Examples project
  lazy val examples = (project in file("examples"))
    .settings(
      commonSettings,
      name := "examples",
      publish / skip := true,
      libraryDependencies ++= Groups.testing ++ Groups.logging
    )
    .dependsOn(core, utils)
  
  // Spark Core project with Scala 2.13 (Spark doesn't support Scala 3 yet)
  lazy val `spark-core` = (project in file("spark-core"))
    .settings(
      commonSettings,
      name := "spark-core",
      // Override Scala version for this project only
      scalaVersion := "2.13.11",
      libraryDependencies ++= Groups.sparkCore ++ Groups.testing213 ++ Groups.logging213 ++ Groups.sparkTesting ++ Seq(Core.typesafeConfig),
      // Spark-specific settings
      Test / fork := true,
      Test / javaOptions ++= Seq(
        "-Dspark.master=local[*]",
        "-Dspark.app.name=singularity-spark-test",
        "-Dlog4j.configurationFile=log4j2.xml",
        "-Dspark.sql.adaptive.logLevel=WARN"
      ),
      // Include test resources
      Test / resourceDirectory := baseDirectory.value / "src" / "test" / "resources",
      // Example of subproject-level dependency override
      dependencyOverrides ++= Seq(
        // Override Jackson version for this project only - use compatible version
        "com.fasterxml.jackson.core" % "jackson-core" % "2.17.2",
        "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.2",
        // Override zstd-jni to resolve version conflicts
        "com.github.luben" % "zstd-jni" % "1.5.6-5"
      ),
      // Allow dependency eviction for version conflicts
      evictionErrorLevel := Level.Warn
    )
    // Note: Not depending on core to avoid Scala version conflicts
    // .dependsOn(core)
  
  // Analytics project with dependency overrides example
  lazy val analytics = (project in file("analytics"))
    .settings(
      commonSettings,
      name := "analytics",
      libraryDependencies ++= Groups.testing ++ Groups.logging ++ Seq(Core.typesafeConfig),
      // Example: Override specific dependencies for this project
      dependencyOverrides ++= Seq(
        // Override Jackson version for this project only
        "com.fasterxml.jackson.core" % "jackson-core" % "2.17.2",
        "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.2",
        // Override logging version
        "ch.qos.logback" % "logback-classic" % "1.3.8"
      ),
      // Add additional dependencies specific to analytics
      libraryDependencies ++= Seq(
        "org.apache.commons" % "commons-math3" % "3.6.1",
        "org.apache.commons" % "commons-lang3" % "3.12.0"
      )
    )
    .dependsOn(core, utils)
}