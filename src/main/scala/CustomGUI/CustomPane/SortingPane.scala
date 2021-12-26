import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.Label
import scalafx.scene.control.TextField
import scalafx.geometry.Pos
import scalafx.scene.control.CheckBox
import scalafx.scene.control.ComboBox
import scalafx.scene.layout.FlowPane
import scalafx.scene.control.TextArea
import scalafx.scene.control.Button

class SortingPane extends FlowPane {
  maxWidth = 200

  val comboBox = new ComboBox(Category.values.toIndexedSeq) {
    minWidth = 300
  }
  comboBox.getSelectionModel.selectLast

  val textArea = new TextArea {
    maxWidth = 300
  }

  val attributePaneSeq = Seq(
    new AttributePane("Name"),
    new AttributePane("Genre"),
    new AttributePane("Category") {
      center = comboBox
    },
    new AttributePane("Page Numbers"),
    new AttributePane("Rating"),
    new AttributePane("Extra Material:"),
    new AttributePane("RPG"),
    new AttributePane("Tags") {
      center = textArea
    }
  )

  children = attributePaneSeq
  children ++= Seq(
    new Button("Search") {
      onAction = () => {
        PanelHandler.centerView.refresh(
          new SearchQuery(
            attributePaneSeq(0).value,
            attributePaneSeq(1).value
          )
        )
      }
    },
    new Button("Clear") {
      onAction = () => {}
    }
  )
}
