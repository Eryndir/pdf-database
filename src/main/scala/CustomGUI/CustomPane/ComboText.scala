import scalafx.scene.layout.BorderPane
import scalafx.scene.control.TextField
import scalafx.scene.control.Label
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.geometry.Pos

case class ComboText(lText: String) extends BorderPane {
  minWidth = 600
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  left = new Label(lText) {
    alignmentInParent = Pos.Center
  }
  val textfield = new TextField {
    alignmentInParent = Pos.Center
    minWidth = 400
  }
  right = textfield

  def update(value: String) = {
    textfield.text = value
  }
  def clear() = {
    textfield.clear
  }
  def text = {
    textfield.text.value
  }
}
