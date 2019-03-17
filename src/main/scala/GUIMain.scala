import java.awt.{Dimension, Point}

import GUI.SpreadSheet
import scheduler._
import util._

import scala.swing.{MainFrame, SimpleSwingApplication}

object GUIMain extends SimpleSwingApplication {
  def top = new MainFrame {
    val Saehyeun = "Schedule1.txt"
    // TODO 당직, 식지인 경우도 빼지 않고 넣기
    val Youngseo = "Schedule2.txt"
    val beforeTwo: List[DayNight] = FileProcessor.extraReader("extra.txt")
    val scheduler: Scheduler = new Scheduler(FileProcessor.fileReader(Saehyeun, false), FileProcessor.fileReader(Youngseo, true), beforeTwo)
    val (program, sc1) = scheduler.makeSchedule(1, 2).unzip
    // TODO 뭔가 너무 쓸데없는 코드 같은 느낌 방법없나?
    val (month, startDoW) = sc1(0) match {
      case Day(_,m,_,d) => (m, d)
      case Night(_,m,_,d) => (m, d)
      case Off(_,m,_,d) => (m, d)
      case Holiday(_,m,_,d) => (m, d)
    }
    title = "Muscle " + month.toString + "월"
    contents = new SpreadSheet(5, 7, program, startDoW)
    size = new Dimension(600,350)
    location = new Point(1200, 100)
  }
}
