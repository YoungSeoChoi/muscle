import scheduler._

import scala.io.Source

object MuscleMain {
  def main(args: Array[String]): Unit = {
    val Saehyeun = "Schedule1.txt"
    val Youngseo = "Schedule2.txt"
    val scheduler: Scheduler = new Scheduler(fileReader(Saehyeun), fileReader(Youngseo))
  }

  def fileReader(fileName: String): List[WorkTime] = {
    val a: List[String] = Source.fromFile(fileName)("Unicode").getLines.toList
    a.foreach(println)
    for {
      line: String <- a
      (front: List[String], back: List[String]) = line.replace('\t', ' ').split(' ').toList.splitAt(3)
      dateArgs: List[Int] = front.map((x) => x.substring(0, x.length - 1).toInt)
      cal = new ScalaCalendar(dateArgs(0), dateArgs(1), dateArgs(2))
      day: WorkTime = back(0) match {
        case "주" => Day(cal)
        case "야" => Night(cal)
        case "비" => Off(cal)
        case "휴" => Holiday(cal)
        case _ => throw new RuntimeException
      }
    } yield day
  }
}
