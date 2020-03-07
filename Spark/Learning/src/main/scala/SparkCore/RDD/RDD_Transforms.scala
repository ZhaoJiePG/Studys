package RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

object RDD_Transforms extends App{
  //构建spark配置
  val sparkconf = new SparkConf()
    .setMaster("local[2]").setAppName("Rdd_Transforms")

  //构建sparkcontext
  val sc = new SparkContext(sparkconf)

  /**
    * 创建Rdd的三种方式：
    * 1.def makeRDD[T: ClassTag]( seq: Seq[T], numSlices: Int = defaultParallelism): RDD[T]   conf.getInt("spark.default.parallelism", math.max(totalCoreCount.get(), 2))
    * 2.def parallelize[T: ClassTag]( seq: Seq[T], numSlices: Int = defaultParallelism): RDD[T]
    * 3.def makeRDD[T: ClassTag](seq: Seq[(T, Seq[String])]): RDD[T]   可以为单个数据对象设置存放的节点
    */
  //  1、def map[U: ClassTag](f: T => U): RDD[U]  一对一转换
  private val rdd1: RDD[Int] = sc.parallelize(1 to 10,3)//默认两个分区
  private val rdd2 = rdd1.map(1 to _)
  //println(rdd2.collect.mkString(","))

  //  2、def filter(f: T => Boolean): RDD[T]   传入一个Boolean的方法，过滤数据
  private val rdd3: RDD[Int] = rdd1.filter(_%3==0)
  //println(rdd3.collect.mkString(","))

  //  3、def flatMap[U: ClassTag](f: T => TraversableOnce[U]): RDD[U]  一对多，并将多压平
  private val rdd4: RDD[Int] = rdd1.flatMap(1 to _)
  //println(rdd4.collect.mkString(","))

  //  4、def mapPartitions[U: ClassTag](f: Iterator[T] => Iterator[U], preservesPartitioning: Boolean = false): RDD[U]  对于一个分区中的所有数据执行一个函数，性能比map要高
  val rdd5 = rdd1.mapPartitions(_.filter(_%3 == 0).map(_+"Hello"))
  //println(rdd5.collect().mkString(","))

  //  5、def mapPartitionsWithIndex[U: ClassTag](f: (Int, Iterator[T]) => Iterator[U], preservesPartitioning: Boolean = false): RDD[U]
  val rdd6 = rdd1.mapPartitionsWithIndex((i,items)=>Iterator(i + ":["+items.mkString(",")+"]"))
  //println(rdd6.collect().mkString(","))

  //  6、def sample(withReplacement: Boolean,fraction: Double,seed: Long = Utils.random.nextLong): RDD[T]  主要用于抽样
  val rdd7 = rdd1.sample(withReplacement = true,0.3,1)
  //println(rdd7.collect().mkString(","))

  //  7、def union(other: RDD[T]): RDD[T]  联合一个RDD，返回组合的RDD
  val rdd8 = rdd1.union(sc.makeRDD(1 to 10))
  //println(rdd8.collect().mkString(","))

  //  8、def intersection(other: RDD[T]): RDD[T]  求交集
  val rdd9 = rdd1.intersection(sc.parallelize(1 to 5))
  //println(rdd9.collect().mkString(","))

  //  9、def distinct(): RDD[T]  去重->shuffle 不排序
  //println(rdd8.distinct(1).collect().sorted.mkString(","))

  //  10、def partitionBy(partitioner: Partitioner): RDD[(K, V)]  用提供的分区器分区
  val rdd10 = rdd1.map(x =>(x,x)).partitionBy(new HashPartitioner(4))
  //println(rdd10.mapPartitionsWithIndex((i,it)=>Iterator(i+":["+it.mkString(",")+"]")).collect().mkString(","))

  //  11、def reduceByKey(func: (V, V) => V): RDD[(K, V)]  根据Key进行聚合  预聚合。
  val rdd11 = rdd1.map((_,1)).reduceByKey(_+_)
  //println(rdd11.collect().mkString(","))

  //  12、def groupByKey(partitioner: Partitioner): RDD[(K, Iterable[V])]  将key相同的value聚集在一起。
  val rdd12 = sc.makeRDD(Array((1,1),(2,1),(2,2),(1,2),(3,1),(3,2),(3,3),(1,0)))
    .groupByKey()
  //println(rdd12.collect().mkString(","))

  //  13、def combineByKey[C](
  // createCombiner: V => C,同一分区中创建临时结构
  // mergeValue: (C, V) => C,同一分区中做逻辑操作
  // mergeCombiners: (C, C) => C,不同分区聚合
  // numPartitions: Int): RDD[(K, C)]
  //求平均分
  val scores = Array(("Fred",88),("Fred",95),("Fred",80),("ZJ",99),("ZJ",69),("ZJ",90),("QQ",90),("QQ",88))
  val rdd13 =sc.parallelize(scores).combineByKey(
    score => (score,1),
    (score1 :(Int,Int) ,score2) => (score1._1 + score2,score1._2 +1),
    (sum1:(Int,Int),sum2:(Int,Int)) => (sum1._1+sum2._1,sum1._2+sum2._2)
  )//.map{case (k,v:(Int,Int))=>(k,v._1/v._2)}
      .map(a =>(a._1,a._2._1/a._2._2))//推荐用case
  //println(rdd13.collect().mkString(","))

  //  14、def aggregateByKey[U: ClassTag](zeroValue: U, partitioner: Partitioner)(seqOp: (U, V) => U,combOp: (U, U) => U): RDD[(K, U)]
  // 是CombineByKey的简化版，可以通过zeroValue直接提供一个初始值。
  val list = List((1,3),(1,2),(1,4),(2,3),(3,6),(3,8))
  //求最大聚合
  val rdd14 = sc.parallelize(list,1)
    //.aggregateByKey(0)(math.max(_,_),_+_)
    //求最大值
    //.aggregateByKey(0)(math.max(_,_),math.max(_,_))
    //求平均值
    .aggregateByKey(0)(_+_,_+_)
  val rdd15 = sc.parallelize(list,1)
    .combineByKey(
      v => math.max(v,0),
      (c : Int,v1) => math.max(c,v1),
      (c1:Int,c2:Int) => c1 + c2
    )
//   求最大值
  // .combineByKey(
//        v => math.max(v,0),
//        (c : Int,v1) => math.max(c,v1),
//        (c1:Int,c2:Int) => c1 + c2
//      )
  //println(rdd14.collect().mkString(","))

  //  15、def foldByKey(zeroValue: V, partitioner: Partitioner)(func: (V, V) => V): RDD[(K, V)]  该函数为aggregateByKey的简化版，seqOp和combOp一样，相同。
  val rdd16 = sc.parallelize(list,1).foldByKey(0)(math.max(_,_))//求和.(_+_)
  //println(rdd16.collect().mkString(","))

  //  16、def sortByKey(ascending: Boolean = true, numPartitions: Int = self.partitions.length) : RDD[(K, V)]  根据Key来进行排序，如果Key目前不支持排序，需要with Ordering接口，实现compare方法，告诉spark key的大小判定。
  val rdd17 = sc.parallelize(list,1).sortByKey()
  //println(rdd17.collect().mkString(","))

  //  17、def sortBy[K]( f: (T) => K, ascending: Boolean = true, numPartitions: Int = this.partitions.length) (implicit ord: Ordering[K], ctag: ClassTag[K]): RDD[T]   根据f函数提供可以排序的key
  val rdd18 = sc.parallelize(list,1).sortBy(_._1)
  //println(rdd18.collect().mkString(","))

  //  18、def join[W](other: RDD[(K, W)], partitioner: Partitioner): RDD[(K, (V, W))]  连接两个RDD的数据。
  //  JOIN ：  只留下双方都有KEY
  //  left JOIN：  留下左边RDD所有的数据
  //  right JOIN： 留下右边RDD所有的数据

  //  19、def cogroup[W](other: RDD[(K, W)], partitioner: Partitioner) : RDD[(K, (Iterable[V], Iterable[W]))]
  // 分别将相同key的数据聚集在一起。

  //  20、def cartesian[U: ClassTag](other: RDD[U]): RDD[(T, U)]  做笛卡尔积。  n * m

  //  21、def pipe(command: String): RDD[String] 执行外部脚本(shell脚本)
  val rdd19 = sc.parallelize(List("aa","bb","cc","dd","ff","gg"),1).pipe("shell脚本")

  //  22、def coalesce(numPartitions: Int, shuffle: Boolean = false,partitionCoalescer: Option[PartitionCoalescer] = Option.empty)(implicit ord: Ordering[T] = null)
  //  : RDD[T]   缩减分区数，用于大数据集过滤后，提高小数据集的执行效率,不进行shuffle操作。

  //  23、def repartition(numPartitions: Int)(implicit ord: Ordering[T] = null): RDD[T] 重新分区。

  //  24、def repartitionAndSortWithinPartitions(partitioner: Partitioner): RDD[(K, V)]  如果重新分区后需要排序，那么直接用这个。

  //  25、def glom(): RDD[Array[T]],数据集转换成Array
  val rdd20 = sc.makeRDD(1 to 10,2).glom()

  //  26、def mapValues[U](f: V => U): RDD[(K, U)] 对于KV结构RDD，只处理value

  //  27、def subtract(other: RDD[T]): RDD[T]  去掉和other重复的元素
}
