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
import State._

class RadioCustom(bText: String, tg: ToggleGroup, bSelect: Boolean)
    extends RadioButton:
  styleClass -= "radio-button"
  styleClass += "toggle-button"
  minHeight = 50
  minWidth = 100

  text = bText
  toggleGroup = tg
  selected = bSelect

  onAction = () =>
    text.value match

      case "View"   => PanelHandler.update(VIEW)
      case "Create" => PanelHandler.update(CREATE)
