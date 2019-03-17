import scheduler._
import util._


object MuscleMain {
  def main(args: Array[String]): Unit = {
    val Saehyeun = "Schedule1.txt"
    // TODO 당직, 식지인 경우도 빼지 않고 넣기
    val Youngseo = "Schedule2.txt"
    val beforeTwo: List[DayNight] = FileProcessor.extraReader("extra.txt")
    val scheduler: Scheduler = new Scheduler(FileProcessor.fileReader(Saehyeun, false), FileProcessor.fileReader(Youngseo, true), beforeTwo)
    val program = scheduler.makeSchedule(1, 2)
    program.foreach(println)
  }
}
