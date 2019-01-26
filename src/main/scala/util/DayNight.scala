package util

// using for exercise program list
abstract class DayNight

case class D() extends DayNight {
  override def toString: String = {
    "낮"
  }
}
case class N() extends DayNight {
  override def toString: String = {
    "밤"
  }
}
