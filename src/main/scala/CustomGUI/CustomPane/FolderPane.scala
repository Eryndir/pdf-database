import scalafx.scene.layout.TilePane
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.AnchorPane
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane
import scalafx.scene.input.MouseEvent
import scalafx.Includes.eventClosureWrapperWithZeroParam

class FolderPane extends FlowPane {
  styleClass += "folder-pane"
  prefWrapLength = 110 * 5
  vgap = 10

}

case class ComboPane(labelText: String, folder: FolderPane, tiles: FlowPane)
    extends BorderPane {
  folder.visible = false
  folder.managed = false
  top = new BorderPane {
    center = new Label(labelText) {
      styleClass += "folder-label"
      onMouseClicked = () => {
        folder.visible = !folder.isVisible
        folder.managed = !folder.isManaged
        tiles.requestLayout
      }
    }
  }
  folder.setId(labelText)
  center = folder
}
