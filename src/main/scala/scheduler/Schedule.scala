package scheduler

import util._

/**
  *
  * @param sc1 이세현 일하는 스케쥴
  * @param sc2 최영서 일하는 스케쥴
  * @param beforeTwo 한 달이 시작되기 2일 전에 운동 스케쥴
  * Schedule trait has two sibling class, it is DaySchedule and OpSchedule.
  */
class Scheduler(sc1: List[WorkTime], sc2: List[WorkTime], beforeTwo: List[DayNight]) {

  /**
    * @param exeAmount 운동 횟수를 줄일 수 있는 허용량 1이면 한 번 줄일 수 있음
    * @param exeDivision 운동 분할 수
    * @return 운동 스케쥴 리스트
    * We can choose a lot of amount of exercise or better agreeable exercise hour (0 ~ 5)
    * We can divide body into (0, 2, 3, 5)
    * using greedy algorithm
    */
  def makeSchedule(exeAmount: Int, exeDivision: Int) = {
    assert((0 <= exeAmount) && (exeAmount <= 5))
    assert(List(0,2,3,5).contains(exeDivision))
    val scTotal = sc1 zip sc2

    exeDivision match {
      case 2 => {
        // rawProgram do not consider exercise holiday
        val rawProgram: List[DayNight] = beforeTwo ++ scTotal.map(x => x match {
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
        pieceProgram.map(divider(_, exeDivision)).flatten.drop(2) zip sc1
      }
    }
  }

  /**
    * @param raw 전체 운동 스케쥴을 받음
    * @param pole split 하는데 쓰일 기준점
    * @tparam T 리스트에 들어가는 타입
    * @return 쪼개진 리스트 조각들
    * 다음에 운동 분할 휴일을 편하게 적용하기 위한 중간 과정
    */
  private def Xsplit[T](raw: List[T], pole: T): List[List[T]] = {
    val idx: Int = raw.indexOf(pole)
    if (idx == -1) List(raw)
    else {
      val (f, b) = raw.splitAt(idx)
      List(f) ++ List(List(b.head)) ++ Xsplit(b.tail, pole)
    }
  }

  /**
    * @param in 운동 분할 휴식일을 아직 포함하지 않은 운동 스케쥴 리스트
    * @param divNum 2, 3, 5, 무 분할
    * @return 분할 휴식일을 포함한 운동 스케쥴 리스트
    * 가장 운동을 점심 때 할 수 있는 것을 우선으로 스케쥴을 만든다. 대신 운동하는 횟수는 줄지 않도록
    */
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

  /**
    * @param in 스케쥴의 길이가 (분할 수 + 1)의 배수인 조각
    * @param divNum 분할 수
    * @return 2가지 경우에서 더 낮에 운동하는 횟수
    * 반환되는 두 값을 비교하면서 더 큰 값이 있는 쪽 운동 스케쥴을 선택한다. (더 쾌적한 스케쥴)
    */
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
