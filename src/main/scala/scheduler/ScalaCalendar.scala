package scheduler

import java.util.Calendar

class ScalaCalendar(year: Int, month: Int, day: Int) {

  val date = {
    val x = new Calendar() {
      override def computeTime(): Unit = ???

      override def computeFields(): Unit = ???

      override def add(field: Int, amount: Int): Unit = ???

      override def roll(field: Int, up: Boolean): Unit = ???

      override def getMinimum(field: Int): Int = ???

      override def getMaximum(field: Int): Int = ???

      override def getGreatestMinimum(field: Int): Int = ???

      override def getLeastMaximum(field: Int): Int = ???
    }
    x.set(year, month, day)
  }
}
