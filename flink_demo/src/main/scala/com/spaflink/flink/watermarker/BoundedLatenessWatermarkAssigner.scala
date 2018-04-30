package com.spaflink.flink.watermarker

import com.spaflink.flink.bean.ComputeResult
import com.spaflink.flink.constants.Constants._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

/**
  * Created by luojiangyu on 3/18/18.
  */
class BoundedLatenessWatermarkAssigner(allowLateness: Int) extends AssignerWithPeriodicWatermarks[ComputeResult] {
  private var maxTimestamp = -1L

  override def getCurrentWatermark: Watermark = {
    new Watermark(maxTimestamp - allowLateness * 1000L)
  }

  override def extractTimestamp(t: ComputeResult, l: Long): Long = {
    val timestamp = t.metaData(FIELD_TIMESTAMP_INTERNAL).asInstanceOf[Long]
    if (timestamp > maxTimestamp) {
      maxTimestamp = timestamp
    }
    timestamp
  }
}