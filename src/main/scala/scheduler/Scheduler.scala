package scheduler

import util._

/**
  * 스케쥴러를 하나의 singleton object로 사용하는게 왠지 맘에 듬 솔직히 클래스에서 바꾼 이유는 모르겠음
  */
object Scheduler {

  /**
    * using greedy algorithm
    * 두 개의 스케쥴이 충돌해 운동할 수 없는 날을 표시해서 합친 스케쥴 초안
    * @param sc1 이세현 스케쥴
    * @param sc2 최영서 스케쥴
    * @return
    */
  def makeRaw(sc1: List[WorkTime], sc2: List[WorkTime]): List[DayNight] = {
    val scTotal = sc1 zip sc2
    // rawProgram do not consider exercise holiday
    val rawProgram: List[DayNight] = scTotal.map {
      case (Holiday(_, _, _, _), _) => X()
      case (_, Holiday(_, _, _, _)) => X()
      case (Day(_, _, _, _), Night(_, _, _, _)) => X()
      case (Night(_, _, _, _), Day(_, _, _, _)) => X()
      case (Day(_, _, _, _), _) => N()
      case (_, Day(_, _, _, _)) => N()
      case _ => D()
    }
    rawProgram
  }

  /**
    * 만들어진 raw 스케쥴에 분할 휴식을 추가한다.
    * @param rawProgram 스케쥴 초안
    * @param beforeTwo 월이 시작하기 2일 전까지 운동 일정 (분할 수에 따라 최대 4일까지 필요할 수 있음)
    * @param exeDivision 분할 수
    * @return
    */
  def schedule(rawProgram: List[DayNight], beforeTwo: List[DayNight], exeDivision: Int): List[DayNight] = {
    assert(List(0,2,3,5).contains(exeDivision))
    exeDivision match {
      // piceceProgram is set of consecutive exercise piece
      case 2 =>
        val prime: List[DayNight] = beforeTwo match {
          case List(_, X()) => rawProgram
          case List(X(), _) =>
            rawProgram.head match {
              case X() => rawProgram
              case z => z :: X() :: rawProgram.drop(2)
            }
          case List(_, _) => X() :: rawProgram.tail
          case _ => throw new RuntimeException
        }
        val pieceProgram: List[List[DayNight]] = Xsplit(prime, X())
        pieceProgram.flatMap(divider(_, exeDivision))
    }
  }

  /**
    * 다음에 운동 분할 휴일을 편하게 적용하기 위한 중간 과정
    * @param raw 전체 운동 스케쥴을 받음
    * @param pole split 하는데 쓰일 기준점
    * @tparam T 리스트에 들어가는 타입
    * @return 쪼개진 리스트 조각들
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
    * 가장 운동을 점심 때 할 수 있는 것을 우선으로 스케쥴을 만든다. 대신 운동하는 횟수는 줄지 않도록
    * @param in 운동 분할 휴식일을 아직 포함하지 않은 운동 스케쥴 리스트
    * @param divNum 2, 3, 5, 무 분할
    * @return 분할 휴식일을 포함한 운동 스케쥴 리스트
    */
  private def divider(in: List[DayNight], divNum: Int): List[DayNight] = {
    if (in.isEmpty) return Nil
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
    if (in.isEmpty) Nil
    else if (in.length <= divNum) in
    else {
      val (f, b) = in.splitAt(divNum)
      f ++ List(X()) ++ dividerInternal(b.tail, divNum)
    }
  }

  /**
    * 반환되는 두 값을 비교하면서 더 큰 값이 있는 쪽 운동 스케쥴을 선택한다. (더 쾌적한 스케쥴)
    * @param in 스케쥴의 길이가 (분할 수 + 1)의 배수인 조각
    * @param divNum 분할 수
    * @return 2가지 경우에서 더 낮에 운동하는 횟수
    */
  private def dayCounter(in: List[DayNight], divNum: Int): (Int, Int) = {
    if (in.isEmpty) (0, 0)
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

  // TODO def save 수정한 일정을 json 형식으로 저장
  // def changeMode(exeAmount: Int, exeDivision: Int)

}

