package GUI

import util._

import scala.swing.{Alignment, Component, Label, ListView, ScrollPane, Table, Color}

class SpreadSheet(height: Int, width: Int, program: List[DayNight], startDoW: DoW) extends ScrollPane {
  val table = new Table(height, width) {
    rowHeight = 40
    autoResizeMode = Table.AutoResizeMode.Off
    showGrid = true
    gridColor = new Color(150, 150, 150)

    override def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      val start = startDoW match {
        case Mon() => 0
        case Tue() => 1
        case Wed() => 2
        case Thu() => 3
        case Fri() => 4
        case Sat() => 5
        case Sun() => 6
      }
      val day = row * 7 + (column - start)
      if (day >= 0 && program.length > day) {
        new Label((day + 1).toString + "일  " + program(day).toString) {
          xAlignment = Alignment.Center
          // TODO 배경색이 안먹는 이유? Label의 배경색과 table의 배경색이 다른 듯... 일단 스킵
        }
      } else new Label((""))
    }
  }

  val rowHeader = new ListView((1.until(height + 1)).map(_.toString)) {
    fixedCellWidth = 20
    fixedCellHeight = table.rowHeight
  }

  val columnHeader = new ListView(List(Mon(), Tue(), Wed(), Thu(), Fri(), Sat(), Sun()).map(_.toString)) {
    fixedCellHeight = table.rowHeight
    fixedCellWidth = 30
  }

  viewportView = table
  rowHeaderView = rowHeader
  columnHeaderView = columnHeader
}
