import scalafx.scene.layout._
import scalafx.scene.control.Label
import scalafx.scene.input.MouseEvent
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.geometry.Pos

case class FolderPane(parentFolder: Pane = null) extends FlowPane:
  styleClass += "folder-pane"
  prefWrapLength = 110 * 5
  vgap = 10
