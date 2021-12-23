import scalafx.scene.control.RadioButton
import scalafx.scene.text.TextAlignment
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.beans.property.StringProperty
import os.Path
case class PdfPane(source: Path = os.pwd) extends RadioButton {
  styleClass -= "radio-button"
  styleClass += "pdf-pane"
  minHeight = 155
  minWidth = 110

  maxHeight = 155
  maxWidth = 110

  wrapText = true
  textAlignment = TextAlignment.Center

  onAction = () => {
    PanelHandler.rightCreate.update(this)
  }
}
