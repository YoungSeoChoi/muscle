package GUI

import java.awt.Color

import util._

import scala.swing.{Alignment, Component, Label, ListView, ScrollPane, Table}

class SpreadSheet(height: Int, width: Int, program: List[DayNight], startDoW: DoW) extends ScrollPane {
  val table = new Table(height, width) {
    rowHeight = 40
    autoResizeMode = Table.AutoResizeMode.Off
    showGrid = true
    gridColor = new Color(150, 150, 150)

    override def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      val start = startDoW match {
        case Mon() => (0, 0)
        case Tue() => (0, 1)
        case Wed() => (0, 2)
        case Thu() => (0, 3)
        case Fri() => (0, 4)
        case Sat() => (0, 5)
        case Sun() => (0, 6)
      }
      val day = (row - start._1) * 7 + (column - start._2)
      if (day >= 0 && program.length > day) {
        new Label((day + 1).toString + "일  " + program(day).toString) {
          xAlignment = Alignment.Center
          background = program(row) match {
            case D() => new Color(217, 108, 132)
            case N() => new Color(115, 142, 223)
            case X() => new Color(88, 88, 88)
          }
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
