package com.rxt.common.flink.realtime.window

import com.rxt.common.flink.realtime.bean.CarWc
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * 会话窗口：n秒中没来数据的话，把前面的统计出来
  */
object SessionWindow {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    //    //按流读文件
    //    env.readFileStream("file:///test.log")
    //      .map(line => {
    //        val tokens = line.split(",")
    //        tokens
    //      })
    //      .filter(_.length == 3)
    //      .map(tokens => CarWc(tokens(0).trim.toInt, tokens(1).trim.toLong, tokens(2).trim.toLong))
    //      .keyBy(0)
    //      .window(ProcessingTimeSessionWindows.withGap(Time.seconds(10)))
    //      .max(1)
    //      .print()

    //按流接收网络数据
    env.socketTextStream("127.0.0.1", 7777)
      .map(line => {
        val tokens = line.split(",")
        tokens
      })
      .filter(_.length == 3)
      .map(tokens => CarWc(tokens(0).trim.toInt, tokens(1).trim.toLong, tokens(2).trim.toLong))
      .keyBy(0)
      .window(ProcessingTimeSessionWindows.withGap(Time.seconds(2)))    //2秒中没来数据的话，把前面的数据统计出来
      .max(2)
      .print()

    env.execute("word count")

  }
}
