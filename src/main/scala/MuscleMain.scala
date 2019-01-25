import scheduler._
import util._

import scala.collection.immutable.HashMap
import scala.io.Source

object MuscleMain {
  def main(args: Array[String]): Unit = {
    val Saehyeun = "Schedule1.txt"
    val Youngseo = "Schedule2.txt"
    val scheduler: Scheduler = new Scheduler(fileReader(Saehyeun), fileReader(Youngseo))
  }

  def fileReader(fileName: String): List[WorkTime] = {
    val a: List[String] = Source.fromFile(fileName)("Unicode").getLines.toList
    for {
      line: String <- a
      (front: List[String], back: List[String]) = line.replace('\t', ' ').split(' ').toList.splitAt(3)
      dateArgs: List[Int] = front.map(x => x.substring(0, x.length - 1).toInt)
      (f, b) = List(Mon(), Tue(), Wed(), Thu(), Fri(), Sat(), Sun()).splitAt(calculateDay(dateArgs(0), dateArgs(1), dateArgs(2)))
      dayIter: Iterator[DoW] = (b ++ f).toIterator
      day: WorkTime = back.head match {
        case "주" => Day(dateArgs(0), dateArgs(1), dateArgs(2),  dayIter.next())
        case "야" => Night(dateArgs(0), dateArgs(1), dateArgs(2),  dayIter.next())
        case "비" => Off(dateArgs(0), dateArgs(1), dateArgs(2),  dayIter.next())
        case "휴" => Holiday(dateArgs(0), dateArgs(1), dateArgs(2),  dayIter.next())
        case _ => throw new RuntimeException
      }
    } yield day
  }

  def calculateDay(year: Int, month: Int, date: Int): Int = {
    val monthDiffList: HashMap[Int, Int] = HashMap(1->0, 2->31, 3->59, 4->90, 5->120, 6->151, 7->181, 8->212, 0->243, 10->273, 11->304, 12->334)
    // Have to update after 2021
    val yearDiffList: HashMap[Int, Int] = HashMap(2018->0, 2019->365, 2020->730, 2021->1096)
    val dateDiff = date + monthDiffList(month) + yearDiffList(year) - 1
    dateDiff % 7
  }
}
