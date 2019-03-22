import java.awt.{Dimension, Point}

import gui.view.SpreadSheet

import scala.swing.{MainFrame, SimpleSwingApplication}

object GUIMain extends SimpleSwingApplication {
  def top: MainFrame = new MainFrame {
    // title 어떻게 바꿀 수 있나? 현재 시간?은 아니고..
    title = "Muscle 3월"
    contents = new SpreadSheet(5, 7)
    size = new Dimension(600,350)
    location = new Point(1200, 100)
  }
}
