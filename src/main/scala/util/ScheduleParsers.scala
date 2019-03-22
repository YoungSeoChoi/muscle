package util

import scala.util.parsing.combinator.RegexParsers

object ScheduleParsers extends RegexParsers {
  def dayNight: Parser[DayNight] = """[dnxDNX]""".r ^^ {s =>
    s.charAt(0).toUpper match {
      case 'D' => D()
      case 'N' => N()
      case 'X' => X()
    }
  }

  def parse(input: String): Option[DayNight] = parseAll(dayNight, input) match {
    case Success(e, _) => Some(e)
    case _: NoSuccess => None
  }
}
