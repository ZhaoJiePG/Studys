package LinkedList


import util.control.Breaks._

/**
  * Created by ZJ on 2019-6-26
  * comment:单向列表
  */
object SongleLinkedListDemo {
  def main(args: Array[String]): Unit = {
    //测试单向列表的添加和遍历
    val hero1 = new HeroNode(1, "宋江", "及时雨")
    val hero2 = new HeroNode(3, "宋江", "及时雨3")
    val hero3 = new HeroNode(4, "宋江", "及时雨4")
    val hero4 = new HeroNode(2, "宋江", "及时雨2")
    val hero5 = new HeroNode(2, "吴用", "智多星")

    //创建一个单向列表
    val singleLinkedList = new SingleLinkedList()
    singleLinkedList.add2(hero1)
    singleLinkedList.add2(hero2)
    singleLinkedList.add2(hero3)
    singleLinkedList.add2(hero4)
    singleLinkedList.update(hero5)
    singleLinkedList.list()

    println("****测试删除****")
    singleLinkedList.delete(2)
    singleLinkedList.list()
  }
}

//定义单向列表管理Hero
class SingleLinkedList {

  //先定义一个头节点，头节点一般不会动
  val head = new HeroNode(0, "", "")

  //删除节点:
  // 1.head不能动
  // 2.使用temp节点，我们要删除temp的下一个节点
  // 3.我们比较的时候，始终是temp.next
  def delete(no:Int)={
    var temp: HeroNode = head
    //标志变量用于确定是否有要删除的节点
    var flag = false
    breakable{
      while (true){
        if (temp.next == null){
          break()
        }
        if (temp.next.no == no){
          flag = true
          break()
        }
        temp = temp.next //temp后移
      }
    }
   if (flag){
     //可以删除,将当前的next指向下一个删除节点的next
     temp.next=temp.next.next
   }else{
     printf("要删除的no=%d 不存在",no)
   }

  }

  //修改节点的值，根据no编号修改，no不变
  def update(newHeroNode: HeroNode): Any = {
    if (head.next == null) {
      println("链表为空")
      return
    }
    //先找节点
    var temp: HeroNode = head.next
    var flag = false
    breakable {
      while (true) {
        if (temp == null) {
          break()
        }
        if (temp.no == newHeroNode.no) {
          //找到
          flag = true
          break()
        }
        temp = temp.next
      }
    }
    //判断是否找到
    if(flag){
      temp.name = newHeroNode.name
      temp.nickname = newHeroNode.nickname
    }else{
      printf("没有找到 编号 为%d 节点，不能修改",newHeroNode.no)
    }
  }

  //编写添加方法
  //第一种方法在添加英雄时，直接添加到链表的尾部
  def add(heroNode: HeroNode): Unit = {
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    var temp: HeroNode = head
    //找到链表的最后
    breakable {
      while (true) {
        if (temp.next == null) { //说明 temp 已经是链表最后
          break()
        }
        //如果没有到最后
        temp = temp.next
      }
    }
    //当退出 while 循环后，temp 就是链表的最后
    temp.next = heroNode
  }

  //第二种添加方式，根据排名添加到指定位置
  //如果有这个排名，则添加失败，并给胡提示
  def add2(heroNode: HeroNode): Unit = {
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    //假设编号从小到大排
    //注意：我们在找添加位置时，时将这个节点添加到temp后面
    //因此，在比较时，是将当前的heroNode和temp.next进行比较
    var temp: HeroNode = head
    //flag用于判断是否该英雄的编号是否已经存在
    var flag = false
    breakable {
      while (true) {
        if (temp.next == null) {
          //说明temp已经是链表的最后
          break()
        }
        //位置找到，当前这个节点因该加到temp后面
        if (temp.next.no > heroNode.no) {
          break()
        } else if (temp.next.no == heroNode.no) {
          //说明已经存在
          flag = true
          break()
        }
        //跳到下一个节点
        temp = temp.next
      }
    }
    //判断flag状态
    if (flag) {
      //不可以添加
      printf("带插入的英雄编号 %d 已经存在", heroNode.no)
    } else {
      //可以添加
      //先让新节点next接入下个节点
      heroNode.next = temp.next
      //再把新节点加到temp上
      temp.next = heroNode
    }
  }

  //遍历单向列表
  def list(): Unit = {

    //判断当前链表是否为空
    if (head.next == null) {
      println("链表为空!!")
      return
    }
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    //因为 head 结点数据，我们不关心，因此这里 temp=head.next
    var temp: HeroNode = head.next
    breakable {
      while (true) {
        //判断是否到最后
        if (temp == null) {
          break()
        }
        printf("结点信息 no=%d name=%s nickname=%s\n",
          temp.no, temp.name, temp.nickname)
        temp = temp.next
      }
    }
  }
}


//先创建HeroNode
class HeroNode(hNo: Int, hName: String, hNickname: String) {
  var no: Int = hNo
  var name: String = hName
  var nickname: String = hNickname
  var next: HeroNode = null //next 默认为null
}
