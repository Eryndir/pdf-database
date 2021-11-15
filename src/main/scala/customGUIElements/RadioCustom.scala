package customGUIElements

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene._
import scalafx.scene.control._
import scalafx.stage.DirectoryChooser
import scalafx.event.ActionEvent
import scalafx.geometry._
import scalafx.css.Styleable
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps

class RadioCustom(textArg: String, selectedArg: Boolean, tg: ToggleGroup)
    extends RadioButton {
  text = textArg
  styleClass -= "radio-button"
  styleClass += "toggle-button"
  minHeight = 50
  minWidth = 100
  toggleGroup = tg
  selected = selectedArg
}
