import scalafx.scene.layout._
import scalafx.scene.control.Label
import scalafx.scene.input.MouseEvent
import scalafx.Includes.eventClosureWrapperWithZeroParam
import scalafx.geometry.Pos

case class ComboPane(labelText: String, folder: FolderPane, tiles: FlowPane)
    extends BorderPane:
  folder.visible = false
  folder.managed = false
  alignmentInParent = Pos.Center

  folder.id = labelText

  center = folder
  top = new BorderPane:
    center = new Label(labelText):
      styleClass += "folder-label"
      onMouseClicked = () =>
        folder.visible = !folder.isVisible
        folder.managed = !folder.isManaged
        tiles.requestLayout
