package com.singularity.spark.core

import java.sql.Timestamp
import java.util.UUID
import java.util.concurrent.{ScheduledFuture, ScheduledThreadPoolExecutor}

import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.apache.spark.sql.catalyst.plans.logical.{EventTimeTimeout, NoTimeout, ProcessingTimeTimeout}
import org.apache.spark.sql.streaming._
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, SparkSession, functions => F}
import scala.concurrent.duration._
import scala.reflect.runtime.universe.TypeTag

object SparkStreamingJob {
  private lazy val threadFactory = new ThreadFactoryBuilder()
    .setDaemon(true)
    .setNameFormat("dataframe-refresher-%d")
    .build()
}

abstract class SparkStreamingJob[I, O](val name: String, val outputTable: Option[String] = None) extends SparkApp {

  // TODO: rename to readStream?
  protected def inputStream: DataStreamReader

  protected def makeDataset(stream: DataStreamReader): Dataset[I]

  protected def compute(ds: Dataset[I]): Dataset[O]

  protected def write(ds: Dataset[O]): DataStreamWriter[O] = {
    val writer = ds.writeStream.queryName(name)
    if (checkpointLocation.isDefined) writer.option("checkpointLocation", checkpointLocation.get) else writer
  }

  protected def startQuery(writer: DataStreamWriter[O]): StreamingQuery = {
    outputTable match {
      case Some(tableName) => writeToTableSink(writer, tableName)
      case None => writer.start()
    }
  }

  protected def postQueryStart(query: StreamingQuery): StreamingQuery = {
    query
  }

  protected def checkpointLocation: Option[String] = {
    val basePath = config.getString("singularity.spark-streaming.checkpoint-base-path")
    Some(s"$basePath/$name/")
  }

  protected def doAllFn: (DataStreamReader) => StreamingQuery = {
    makeDataset _ andThen
      compute andThen
      write andThen
      startQuery andThen
      postQueryStart
  }

  def execute: StreamingQuery = {
    doAllFn(inputStream)
  }


  protected def writeToTableSink(writer: DataStreamWriter[O], tableName: String): StreamingQuery = {
    val query = writer.toTable(tableName)
    // SparkEventPoster.postLineage
    query
  }
}

abstract class SparkStreamingEtl[I <: Product: TypeTag, O](
                                                          override val name: String
                                                          ) extends SparkStreamingJob[I, O](name) {

}