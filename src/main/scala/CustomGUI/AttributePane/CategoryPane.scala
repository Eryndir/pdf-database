import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control._
import GUI._

class CategoryPane extends AttributePane("Category"):
  val headers = new HeaderAttributePane

  val comboBox = new ComboBox(dbHandler.getCategoryTitles):
    minWidth = 300
    maxWidth = 300
    visibleRowCount = 5

    onAction = () =>
      val strSelected = this.getSelectionModel.getSelectedItem
      if !strSelected.equals("") then
        val tmp = dbHandler.getCategory(strSelected)
        headers.updateLabel(tmp.header1, tmp.header2, tmp.header3)
      else headers.updateLabel("", "", "")

  left = comboBox
  center = null
  bottom = headers

  comboBox.getSelectionModel.selectLast

  override def value: String =
    comboBox.getSelectionModel.getSelectedItem

  def headerValues: (String, String, String) =
    headers.valueTouple

  def updateValue(strh1: String, strh2: String, strh3: String) =
    headers.updateValue(strh1, strh2, strh3)

  def selectCategory(strCat: String) =
    comboBox.getSelectionModel.select(strCat)
    val tmp = dbHandler.getCategory(strCat)
    headers.updateLabel(tmp.header1, tmp.header2, tmp.header3)
