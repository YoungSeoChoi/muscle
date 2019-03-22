package util

import scheduler._

import scala.collection.immutable.HashMap
import scala.io.Source

/**
  * 각 일하는 스케쥴이 담겨있는 파일을 읽고 처리
  */
object FileProcessor {
  // excerciseClock is false if someone is OP worker
  def fileReader(fileName: String, exerciseClock: Boolean): List[WorkTime] = {
    // No ANSI encoding!
    val a: List[String] = Source.fromFile(fileName)("Unicode").getLines.toList
    for {
      line: String <- a
      (front: List[String], back: List[String]) = line.replace('\t', ' ').split(' ').toList.splitAt(3)
      dateArgs: List[Int] = front.map(x => x.substring(0, x.length - 1).toInt)
      (f, b) = List(Mon(), Tue(), Wed(), Thu(), Fri(), Sat(), Sun()).splitAt(calculateDay(dateArgs.head, dateArgs(1), dateArgs(2)))
      // XXX Why this iterator doesn't blow up when it comes to the end?
      dayIter: Iterator[DoW] = (b ++ f).toIterator
      day: WorkTime = back.head match {
        case "주" =>
          val dow = dayIter.next()
          if (exerciseClock) {
            dow match {
              case Wed() => Off(dateArgs.head, dateArgs(1), dateArgs(2), Wed())
              case _ => Day(dateArgs.head, dateArgs(1), dateArgs(2), dow)
            }
          } else Day(dateArgs.head, dateArgs(1), dateArgs(2), dow)
        case "야" => Night(dateArgs.head, dateArgs(1), dateArgs(2),  dayIter.next())
        case "비" => Off(dateArgs.head, dateArgs(1), dateArgs(2),  dayIter.next())
        case "휴" => Holiday(dateArgs.head, dateArgs(1), dateArgs(2),  dayIter.next())
        case d => println("message: " + d.toString); throw new RuntimeException
      }
    } yield day
  }

  /**
    * 날짜 계산해서 요일 확인하는데 쓰임
    * @param year year
    * @param month month
    * @param date date
    * @return
    */
  def calculateDay(year: Int, month: Int, date: Int): Int = {
    val monthDiffList: HashMap[Int, Int] = HashMap(1->0, 2->31, 3->59, 4->90, 5->120, 6->151, 7->181, 8->212, 0->243, 10->273, 11->304, 12->334)
    // Have to update after 2021
    val yearDiffList: HashMap[Int, Int] = HashMap(2018->0, 2019->365, 2020->730, 2021->1096)
    val dateDiff = date + monthDiffList(month) + yearDiffList(year) - 1
    dateDiff % 7
  }

  /**
    * 한 달이 시작되기 2일 전 운동 스케쥴 2개를 받아옴
    * @param fileName extra file name
    * @return
    */
  def extraReader(fileName: String): List[DayNight] = {
    // TODO 분할 수에 따라 필요한 스케쥴 수가 다르기 때문에 넉넉하게 4개를 가져와야함 (최대 5분할)
    val a: List[String] = Source.fromFile(fileName)("Unicode").getLines.toList
    a.map {
      case "D" => D()
      case "N" => N()
      case "X" => X()
      case _ => throw new RuntimeException
    }
  }
}
