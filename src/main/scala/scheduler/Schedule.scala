package scheduler

import util._

/*
* Schedule trait has two sibling class, it is DaySchedule and OpSchedule.
*/
class Scheduler(sc1: List[WorkTime], sc2: List[WorkTime]) {

  // We can choose a lot of amount of exercise or better agreeable exercise hour (0 ~ 5)
  // We can divide body into (0, 2, 3, 5)
  // using greedy algorithm
  def makeSchedule(exeAmount: Int, exeDivision: Int): List[DayNight] = {
    assert((0 <= exeAmount) && (exeAmount <= 5))
    assert(List(0,2,3,5).contains(exeDivision))

//    val program: Array[DayNight] = new Array[DayNight](sc1.length)
//    val initprogram = initSchedule(sc1, sc2, program)
    val scTotal = sc1 zip sc2

    exeDivision match {
      case 2 => {
        // rawProgram do not consider exercise holiday
        val rawProgram: List[DayNight] = scTotal.map(x => x match {
          case (Holiday(_, _, _, _), _) => X()
          case (_, Holiday(_, _, _, _)) => X()
          case (Day(_, _, _, _), Night(_, _, _, _)) => X()
          case (Night(_, _, _, _), Day(_, _, _, _)) => X()
          case (Day(_, _, _, _), _) => N()
          case (_, Day(_, _, _, _)) => N()
          case _ => D()
        })
        // piceceProgram is set of consecutive exercise piece
        val pieceProgram: List[List[DayNight]] = Xsplit(rawProgram, X())
        pieceProgram.map(divider(_, exeDivision)).flatten
      }
    }
  }

  private def Xsplit[T](raw: List[T], pole: T): List[List[T]] = {
    val idx: Int = raw.indexOf(pole)
    if (idx == -1) List(raw)
    else {
      val (f, b) = raw.splitAt(idx)
      List(f) ++ List(List(b.head)) ++ Xsplit(b.tail, pole)
    }
  }

  private def divider(in: List[DayNight], divNum: Int): List[DayNight] = {
    if (in.length == 0) return Nil
    if (in.length <= divNum) return in

    if (in.length % (divNum + 1) == 0) {
      val (x, y) = dayCounter(in, divNum)
      if (x > y) {
        val (f, b) = in.splitAt(divNum)
        f ++ List(X()) ++ dividerInternal(b.tail, divNum)
      } else {
        List(X()) ++ dividerInternal(in.tail, divNum)
      }
    } else {
      val (f, b) = in.splitAt(divNum)
      f ++ List(X()) ++ dividerInternal(b.tail, divNum)
    }
  }

  private def dividerInternal(in: List[DayNight], divNum: Int): List[DayNight] = {
    if (in.length == 0) Nil
    else if (in.length <= divNum) in
    else {
      val (f, b) = in.splitAt(divNum)
      f ++ List(X()) ++ dividerInternal(b.tail, divNum)
    }
  }

  private def dayCounter(in: List[DayNight], divNum: Int): (Int, Int) = {
    if (in.length == 0) (0, 0)
    else {
      val (f, b) = in.splitAt(divNum)
      (f.head, b.head) match {
        // TODO refactor with tail recursion
        case (D(), D()) => val (x, y) = dayCounter(b.tail, divNum)
          (x + 1, y + 1)
        case (D(), _) => val (x, y) = dayCounter(b.tail, divNum)
          (x + 1, y)
        case (_, D()) => val (x, y) = dayCounter(b.tail, divNum)
          (x, y + 1)
        case (_, _) => dayCounter(b.tail, divNum)
      }
    }
  }

//  private def initSchedule(sc1: List[WorkTime], sc2: List[WorkTime], program: Array[DayNight]): Array[DayNight] = {
//  }

  // def changeMode(exeAmount: Int, exeDivision: Int)

  // def reschedule(exeAmount: Int, exeDivision: Int, Date: Int)
}
