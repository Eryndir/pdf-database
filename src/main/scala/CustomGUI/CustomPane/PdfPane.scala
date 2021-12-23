import scalafx.scene.control.RadioButton
import scalafx.scene.text.TextAlignment

class PdfPane extends RadioButton {
  styleClass -= "radio-button"
  styleClass += "toggle-button"
  minHeight = 155
  minWidth = 110

  maxHeight = 155
  maxWidth = 110

  wrapText = true
  textAlignment = TextAlignment.Center
}
