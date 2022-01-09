import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control._
import GUI._
import scalafx.collections.ObservableBuffer

object SystemHelper:
  val systemBuffer = new ObservableBuffer[String]
  systemBuffer.addAll(dbHandler.getSystems)

class SystemPane extends AttributePane("System"):
  textField.minWidth = 250
  textField.maxWidth = 250

  val localBuffer = SystemHelper.systemBuffer

  val comboBox = new ComboBox(localBuffer):
    minWidth = 50
    maxWidth = 50
    visibleRowCount = 5

    onAction = () =>
      val strSelected = this.getSelectionModel.getSelectedItem
      textField.text = strSelected

  left = comboBox

  textField.onAction = () =>
    val system = textField.text.value

    if !system.equals("") then
      localBuffer.add(system)
      dbHandler.addSystem(system)
