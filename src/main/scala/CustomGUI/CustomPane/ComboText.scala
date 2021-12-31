import scalafx.scene.layout.BorderPane
import scalafx.scene.control.TextField
import scalafx.scene.control.Label
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.geometry.Pos

case class ComboText(lText: String) extends BorderPane:
  minWidth = 600
  padding = new javafx.geometry.Insets(10, 10, 10, 10)

  val textField = new TextField:
    alignmentInParent = Pos.Center
    minWidth = 300

  left = new Label(lText) { alignmentInParent = Pos.Center }
  right = textField

  def update(value: String) = textField.text = value
  def clear = textField.clear
  def text = textField.text.value
