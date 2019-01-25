package util

// Day of the Week
abstract class DoW

case class Mon() extends DoW {
  override def toString: String = {
    "월"
  }
}
case class Tue() extends DoW {
  override def toString: String = {
    "화"
  }
}
case class Wed() extends DoW {
  override def toString: String = {
    "수"
  }
}
case class Thu() extends DoW {
  override def toString: String = {
    "목"
  }
}
case class Fri() extends DoW {
  override def toString: String = {
    "금"
  }
}
case class Sat() extends DoW {
  override def toString: String = {
    "토"
  }
}
case class Sun() extends DoW {
  override def toString: String = {
    "일"
  }
}


