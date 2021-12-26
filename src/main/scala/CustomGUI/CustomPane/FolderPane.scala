import scalafx.scene.layout.TilePane
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.AnchorPane
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import scalafx.scene.input.MouseEvent
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.scene.layout.Pane
import scalafx.geometry.Pos

case class FolderPane(parentFolder: Pane = null) extends FlowPane {
  styleClass += "folder-pane"
  prefWrapLength = 110 * 5
  vgap = 10
}
