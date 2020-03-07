package LinkedList

import scala.util.control.Breaks.{break, breakable}

/**
  * Created by ZJ on 2019-7-5
  * comment:双向链表结构
  */
object DoubleLinkedList {
  def main(args: Array[String]): Unit = {

    //测试单向列表的添加和遍历
    val hero1 = new HeroNode2(1, "宋江", "及时雨")
    val hero2 = new HeroNode2(3, "宋江", "及时雨3")
    val hero3 = new HeroNode2(4, "宋江", "及时雨4")
    val hero4 = new HeroNode2(1, "宋江", "及时雨2")
    val hero5 = new HeroNode2(22, "吴用", "智多星")

    //创建一个单向列表
    val doubleLinkedList = new DoubleLinkedList()
    doubleLinkedList.add(hero1)
    doubleLinkedList.add(hero2)
    doubleLinkedList.add(hero3)
    doubleLinkedList.update(hero4)
    doubleLinkedList.list()

    print("====测试删除====")
    doubleLinkedList.delete(3)
    doubleLinkedList.list()
  }
}

class DoubleLinkedList{
  //先定义一个头节点，头节点一般不会动
  val head = new HeroNode2(0, "", "")
  //添加-遍历-修改-删除

  //删除
  //因为双向列表可以实现自我删除
  def delete(no:Int): Any ={
    //判断当前链表是否为空
    if(head.next == null){
      printf("链表为空")
      return
    }
    var temp: HeroNode2 = head.next
    //标志变量用于确定是否有要删除的节点
    var flag = false
    breakable{
      while (true){
        if (temp.next == null){
          break()
        }
        if (temp.no == no){
          flag = true
          break()
        }
        temp = temp.next //temp后移
      }
    }
    if (flag){
      //可以删除,将当前的pre指向当前的next
      temp.pre.next = temp.next
      if(temp.next != null){
        //最后一个为空会为控指针
        temp.next.pre = temp.pre
      }else{
        temp.pre = null
      }
    }else{
      printf("要删除的no=%d 不存在",no)
    }

  }

  //修改节点的值，根据no编号修改，no不变
  def update(newHeroNode: HeroNode2): Any = {
    if (head.next == null) {
      println("链表为空")
      return
    }
    //先找节点
    var temp: HeroNode2 = head.next
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

  //添加(不考虑排序)
  def add(heroNode: HeroNode2): Unit = {
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    var temp: HeroNode2 = head
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
    heroNode.pre = temp
  }

  //遍历列表
  def list(): Unit = {

    //判断当前链表是否为空
    if (head.next == null) {
      println("链表为空!!")
      return
    }
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    //因为 head 结点数据，我们不关心，因此这里 temp=head.next
    var temp: HeroNode2 = head.next
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
class HeroNode2(hNo: Int, hName: String, hNickname: String) {
  var no: Int = hNo
  var name: String = hName
  var nickname: String = hNickname
  var pre :HeroNode2 = null //pre 默认也为null
  var next: HeroNode2 = null //next 默认为null
}
