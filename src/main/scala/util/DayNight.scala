package util

// using for exercise program list
trait DayNight

case class D() extends DayNight {
  override def toString: String = {
    "D"
  }
}
case class N() extends DayNight {
  override def toString: String = {
    "N"
  }
}

case class X() extends DayNight {
  override def toString: String = {
    "X"
  }
}
