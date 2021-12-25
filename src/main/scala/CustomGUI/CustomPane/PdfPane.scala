import scalafx.scene.control.RadioButton
import scalafx.scene.text.TextAlignment
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.beans.property.StringProperty
import os.Path
import scalafx.application.Platform
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Cursor
import State._

case class PdfPane(source: Path = os.pwd, parentCombo: ComboPane = null)
    extends RadioButton {
  styleClass -= "radio-button"
  styleClass += "pdf-pane"
  minHeight = 155
  minWidth = 110
  maxHeight = 155
  maxWidth = 110

  wrapText = true
  textAlignment = TextAlignment.Center

  onAction = () => {
    PanelHandler.panelState match {
      case CREATE => {
        println("lmao")
        Platform.runLater(() -> {
          GUI.stage.getScene.cursor = Cursor.sfxCursor2jfx(Cursor.Wait)
        })
        if !PanelHandler.leftCreate.loading then
          PanelHandler.rightCreate.update(this)
      }
      case VIEW => PanelHandler.rightView.update(this)
    }

  }
}
