package gui.view

import gui.controller.Controller
import gui.model.Model
import scheduler.Scheduler
import util._
import PrettyPrint.pp

import scala.swing.event.TableUpdated
import scala.swing.{Color, Component, Label, ListView, ScrollPane, Table, TextField, event}

class SpreadSheet(height: Int, width: Int) extends ScrollPane {
  case class DayChanged(row: Int, column: Int, after: String) extends event.Event

  val programModel = new Model(height, width)

  def day(r: Int, c: Int): Int = r * 7 + (c - programModel.start)

  val table: Table = new Table(height, width) {
    rowHeight = 40
    autoResizeMode = Table.AutoResizeMode.Off
    showGrid = true
    gridColor = new Color(150, 150, 150)

    override def rendererComponent(isSelected: Boolean, focused: Boolean, row: Int, column: Int): Component = {
      val d = day(row, column)
      if (d >= 0 && d < programModel.programList.length) {
        if (focused) {
          new TextField(userData(row, column))
        }
        else {
          new Label((d + 1).toString + "일  " + programModel.program(d).value.toString)
        }
      } else new Label("")
    }

    /**
      * 잘은 모르겠지만 view 사이드인 table이 보관하고 있는 데이터 같다.
      * @param row table row
      * @param column table column
      * @return view side data
      */
    def userData(row: Int, column: Int): String = {
      val v = this(row, column)
      if (v == null) programModel.rawProgram(day(row, column)).toString else v.toString
    }

    reactions += {
      // TODO 꼭 업데이트를 한 열에 대해 전부 다 받아야 하나? 너무 비효율적으로 보임
      case TableUpdated(_ , rows, column) =>
        for (row <- rows) {
          if (programModel.program(day(row, column)).value.toString != userData(row, column))
            publish(DayChanged(row, column, userData(row, column)))
        }
    }

  }

  val controller: Controller = new Controller {
    listenTo(table)
    reactions += {
      case DayChanged(r, c, v) =>
        val d: Int = day(r, c)
        ScheduleParsers.parse(v) match {
          case Some(p) =>
            println("succ: " + p.toString)
            programModel.program.map(_.value).foreach(pp)
            programModel.rawProgram(d) = p
            val (a: Array[DayNight], b: Array[DayNight]) = programModel.rawProgram.drop(d - 2).splitAt(2)
            val out: List[DayNight] = Scheduler.schedule(b.toList, a.toList, programModel.exeDivision)
            for (i <- d until programModel.program.length) {
              val j = i - d
              programModel.program(i).value = out(j)
            }
            println("\nafter")
            programModel.program.map(_.value).foreach(pp)
            println()
          case None => println("failed parsing")
        }
    }
  }

  val rowHeader: ListView[String] = new ListView(1.until(height + 1).map(_.toString)) {
    fixedCellWidth = 20
    fixedCellHeight = table.rowHeight
  }

  val columnHeader: ListView[String] = new ListView(List(Mon(), Tue(), Wed(), Thu(), Fri(), Sat(), Sun()).map(_.toString)) {
    fixedCellHeight = table.rowHeight
    fixedCellWidth = 30
  }

  viewportView = table
  rowHeaderView = rowHeader
  columnHeaderView = columnHeader
}
