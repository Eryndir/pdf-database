import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control._
import scalafx.geometry.Pos
import scalafx.scene.layout._

class SortingPane extends FlowPane:
  maxWidth = 200

  val comboBox = new ComboBox(Category.values.toIndexedSeq) { minWidth = 300 }

  comboBox.getSelectionModel.selectLast

  val readCheck = new CheckBox
  val favCheck = new CheckBox
  val tagArea = new TagArea { maxWidth = 300 }

  val attributePaneSeq = Seq(
    new AttributePane("Name"),
    new AttributePane("Genre"),
    new AttributePane("Category") { center = comboBox },
    new AttributePane("Page Numbers"):
      textField.minWidth = 100
      textField.maxWidth = 100
    ,
    new AttributePane("Rating"):
      textField.minWidth = 100
      textField.maxWidth = 100
    ,
    new AttributePane("RPG"),
    new AttributePane("Read") { center = readCheck },
    new AttributePane("Favourite") { center = favCheck },
    new AttributePane("Tags") { center = tagArea }
  )

  children = attributePaneSeq

  children += new BorderPane:
    padding = new javafx.geometry.Insets(10, 10, 10, 10)
    alignment = Pos.TopCenter

    left = new Button("Search"):
      onAction = () =>
        val pgNumAttr = attributePaneSeq(3).value
        val tagList = new TagList
        tagList.addList(tagArea.getTags)

        PanelHandler.centerView.refresh(
          new SearchQuery(
            attributePaneSeq(0).value,
            attributePaneSeq(1).value,
            tagList,
            if (pgNumAttr.equals("")) 0 else pgNumAttr.toInt,
            attributePaneSeq(4).value,
            readCheck.isSelected,
            favCheck.isSelected,
            comboBox.value.value,
            attributePaneSeq(5).value
          )
        )

    right = new Button("Clear"):
      onAction = () =>
        attributePaneSeq.map(x => x.clear)
        readCheck.selected = false
        favCheck.selected = false
        comboBox.getSelectionModel.selectLast
        tagArea.clear
