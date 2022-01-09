import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control._
import scalafx.geometry.Pos
import scalafx.scene.layout._
import GUI._

class SortingPane extends FlowPane:
  maxWidth = 200

  val categoryPane = new CategoryPane
  categoryPane.comboBox += ("")
  val systemPane = new SystemPane

  val readCheck = new CheckBox
  val favCheck = new CheckBox
  val tagArea = new TagArea { maxWidth = 300 }

  val attributePaneSeq = Seq(
    new AttributePane("Name"),
    new AttributePane("Genre"),
    categoryPane,
    new AttributePane("Page Numbers"):
      textField.minWidth = 100
      textField.maxWidth = 100
    ,
    new AttributePane("Rating"):
      textField.minWidth = 100
      textField.maxWidth = 100
    ,
    systemPane,
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
            dbHandler.getCategory(categoryPane.value),
            attributePaneSeq(5).value
          )
        )

    right = new Button("Clear"):
      onAction = () =>
        attributePaneSeq.map(x => x.clear)
        readCheck.selected = false
        favCheck.selected = false
        categoryPane.comboBox.getSelectionModel.selectLast
        tagArea.clear
