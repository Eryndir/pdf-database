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
import scalafx.scene.layout.HBox

class SortingPane extends FlowPane {
  maxWidth = 200

  val comboBox = new ComboBox(Category.values.toIndexedSeq) {
    minWidth = 300
  }
  comboBox.getSelectionModel.selectLast

  val tagArea = new TextArea {
    maxWidth = 300
  }

  val favCheck = new CheckBox
  val readCheck = new CheckBox

  val attributePaneSeq = Seq(
    new AttributePane("Name"),
    new AttributePane("Genre"),
    new AttributePane("Category") {
      center = comboBox
    },
    new AttributePane("Page Numbers") {
      textField.minWidth = 100
      textField.maxWidth = 100
    },
    new AttributePane("Rating") {
      textField.minWidth = 100
      textField.maxWidth = 100
    },
    new AttributePane("RPG"),
    new AttributePane("Read") {
      center = readCheck
    },
    new AttributePane("Favourite") {
      center = favCheck
    },
    new AttributePane("Tags") {
      center = tagArea
    }
  )

  children = attributePaneSeq

  children += new HBox {
    padding = new javafx.geometry.Insets(10, 10, 10, 10)
    children = Seq(
      new Button("Search") {
        onAction = () => {
          println("oof")
          println(readCheck.isSelected)
          println("lmao")
          val pgNumAttr = attributePaneSeq(3).value
          PanelHandler.centerView.refresh(
            new SearchQuery(
              attributePaneSeq(0).value,
              attributePaneSeq(1).value,
              tagArea.text.value.split("\n").toList.map(x => x.trim),
              if (pgNumAttr.equals("")) 0 else pgNumAttr.toInt,
              attributePaneSeq(4).value,
              readCheck.isSelected,
              favCheck.isSelected,
              comboBox.value.value,
              attributePaneSeq(5).value
            )
          )
        }
      },
      new Button("Clear") {
        onAction = () => {
          attributePaneSeq.map(x => x.clear())
          readCheck.selected = false
          favCheck.selected = false
          comboBox.getSelectionModel.selectLast
          tagArea.clear
        }
      }
    )

  }
}
