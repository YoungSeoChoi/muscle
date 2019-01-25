package scheduler

/*
* Define the several types of work
*/
abstract class WorkTime

// Day work has end time. For example, Wednesday work is ended at 11:30
case class Day(date: ScalaCalendar) extends WorkTime
// TODO parameter of night work, holiday are not adequate
case class Night(date: ScalaCalendar) extends WorkTime
// Off has two type, night work off and just off
case class Off(date: ScalaCalendar) extends WorkTime // Both night off and just off
case class Holiday(date: ScalaCalendar) extends WorkTime
