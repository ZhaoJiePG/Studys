package RDD

//减少shuffle的步骤
import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

/**
  * Created by ZJ on 2019-3-6
  * comment:
  */
object GroupFavTeacher4 {
  def main(args: Array[String]): Unit = {
    val topN = 3;

    val conf = new SparkConf().setAppName("FavTeacher").setMaster("local[4]")
    val sc = new SparkContext(conf)

    //指定以后从哪里读取数据
    val lines: RDD[String] = sc.textFile("/input/teacher.log")

    //整理数据
    val SubjAndTeatcher: RDD[((String, String), Int)] = lines.map(line=>{
      val index: Int = line.lastIndexOf("/")
      val teacher = line.substring(index + 1)
      val subject = line.substring(0,index).split("[.]")(0).split("/")(2)
      ((subject,teacher),1)
    })

    //计算有多少学科
    val subjects: Array[String] = SubjAndTeatcher.map(_._1._1).distinct().collect()

    val sbPartitioner = new SubjectParitionerr(subjects)

    //聚合:按照指定的分区器进行分区，减少一个shuffle(聚合且分区)
    val reducer: RDD[((String, String), Int)] = SubjAndTeatcher.reduceByKey(sbPartitioner,_+_)


    //分区内部排序,可以只操作一个分区内的数据(迭代器)
    val value: RDD[((String, String), Int)] = reducer.mapPartitions(it => {
      //将迭代器转换为list，再返回迭代器
      it.toList.sortBy(_._2).take(2).iterator
      //大数据量时的操作，即排序又不全部加载到内存，使用可以排序的集合,比较删除
      //val set = new scala.collection.mutable.HashSet(((String, String), Int))
    })
    val res: Array[((String, String), Int)] = value.collect()

    println(res.toBuffer)
    sc.stop()
  }

}

//自定义分区器
class SubjectParitionerr(sbs: Array[String]) extends Partitioner {

  //相当于主构造器（new的时候回执行一次）
  //用于存放规则的一个map
  val rules = new scala.collection.mutable.HashMap[String, Int]()
  var i = 0
  for(sb <- sbs) {
    //rules(sb) = i
    rules.put(sb, i)
    i += 1
  }

  //返回分区的数量（下一个RDD有多少分区）
  override def numPartitions: Int = sbs.length

  //根据传入的key计算分区标号
  //key是一个元组（String， String）
  override def getPartition(key: Any): Int = {
    //获取学科名称
    val subject = key.asInstanceOf[(String, String)]._1
    //根据规则返回分区编号
    //rules.get(subject)
    rules(subject)
  }
}
