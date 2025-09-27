import Dependencies._

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := Versions.scala

ThisBuild / organization := "com.singularity"

lazy val root = Projects.root
lazy val core = Projects.core
lazy val utils = Projects.utils
lazy val examples = Projects.examples
lazy val `spark-core` = Projects.`spark-core`
lazy val analytics = Projects.analytics