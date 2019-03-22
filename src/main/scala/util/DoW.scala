package util

// Day of the Week
trait DoW {
  def toInt: Int
}

case class Mon() extends DoW {
  override def toString: String = {
    "월"
  }

  override def toInt: Int = 0
}
case class Tue() extends DoW {
  override def toString: String = {
    "화"
  }

  override def toInt: Int = 1
}
case class Wed() extends DoW {
  override def toString: String = {
    "수"
  }

  override def toInt: Int = 2
}
case class Thu() extends DoW {
  override def toString: String = {
    "목"
  }

  override def toInt: Int = 3
}
case class Fri() extends DoW {
  override def toString: String = {
    "금"
  }

  override def toInt: Int = 4
}
case class Sat() extends DoW {
  override def toString: String = {
    "토"
  }

  override def toInt: Int = 5
}
case class Sun() extends DoW {
  override def toString: String = {
    "일"
  }

  override def toInt: Int = 6
}


