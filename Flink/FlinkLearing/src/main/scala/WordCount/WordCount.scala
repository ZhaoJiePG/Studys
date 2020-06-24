package com.zj.wc

import org.apache.flink.api.scala._

/**
  * Created by ZJ on 2019-12-13
  * comment:
  *
  * 批处理wordcount
  *
  */
object WordCount {
  def main(args: Array[String]): Unit = {

    // 创建一个批处理的执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    // 从文件中读取数据
    val inputPath = "D:\\Maven\\Studys\\Flink\\FlinkLearing\\src\\main\\Data\\hello.txt"
    val inputDataSet: DataSet[String] = env.readTextFile(inputPath)

    // 分词之后做count
    val wordCountDataSet: AggregateDataSet[(String, Int)] = inputDataSet.flatMap(_.split(" "))
      .map( (_, 1) )
      .groupBy(0)
      .sum(1)

    // 打印输出
    wordCountDataSet.print()
  }

}
