package gui.model

import scheduler._
import util._
import PrettyPrint.pp

class Model(height: Int, width: Int) {
  val Saehyeun = "Schedule1.txt"
  // TODO 당직, 식지인 경우도 빼지 않고 넣기
  val Youngseo = "Schedule2.txt"
  val sc1: List[WorkTime] = FileProcessor.fileReader(Saehyeun, exerciseClock = false)
  val sc2: List[WorkTime] = FileProcessor.fileReader(Youngseo, exerciseClock = true)
  val beforeTwo: List[DayNight] = FileProcessor.extraReader("extra.txt")
  val exeDivision: Int = 2
  val rawProgram = Scheduler.makeRaw(sc1, sc2).toArray
  println("raw: ")
  rawProgram.foreach(pp)
  val programList: List[DayNight] = Scheduler.schedule(rawProgram.toList, beforeTwo, exeDivision)
  val (month, startDoW) = sc1.head match {
    case Day(_,m,_,d) => (m, d)
    case Night(_,m,_,d) => (m, d)
    case Off(_,m,_,d) => (m, d)
    case Holiday(_,m,_,d) => (m, d)
  }
  val start: Int = startDoW.toInt
  // 여기까지 재료 준비 끝

  case class One(num: Int) {
    var v: DayNight = _
    def value: DayNight = v
    def value_=(w: DayNight): Unit = {
      if (v != w) {
        v = w
        // null check 해서 null이었으면 publish 안함
      }
    }
  }

  val program = new Array[One](programList.length)
  for (i <- programList.indices) {
    val one = One(i)
    one.value = programList(i)
    program(i) = one
  }
}
