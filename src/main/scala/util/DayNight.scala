package util

// using for exercise program list
trait DayNight

case class D() extends DayNight {
  override def toString: String = {
    "점심"
  }
}
case class N() extends DayNight {
  override def toString: String = {
    "저녁"
  }
}

case class X() extends DayNight {
  override def toString: String = {
    "X"
  }
}
