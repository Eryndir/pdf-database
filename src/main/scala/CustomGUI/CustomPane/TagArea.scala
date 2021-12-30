import scalafx.scene.control.TextArea
import scalafx.geometry.Pos
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control.TextArea
import scalafx.scene._
import scalafx.scene.control.Label
import scalafx.scene.control.Dialog
import scalafx.application.Platform
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage
import scalafx.application.JFXApp3
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.ListView
import scalafx.scene.control.Button
import scalafx.scene.control.TextField
import scalafx.scene.layout.HBox
import scalafx.beans.Observable
import scalafx.collections.ObservableBuffer
import GUI._

class TagArea extends BorderPane {
  maxHeight = 200
  var text = ""
  val obsAllTags = new ObservableBuffer[String]
  val obsSelcTags = new ObservableBuffer[String]
  obsAllTags.addAll(dbHandler.getTagList)

  val leftList = new ListView(obsAllTags) {
    maxWidth = 150
  }

  val rightList = new ListView(obsSelcTags) {
    maxWidth = 150
  }

  left = leftList
  right = rightList

  bottom = new BorderPane {

    val newTagField = new TextField {
      maxWidth = 150
    }

    left = newTagField
    right = new HBox {
      children = Seq(
        new Button("Add") {
          minWidth = 75
          alignmentInParent = Pos.Center
          onAction = () => {
            val tag = newTagField.text.value
            obsAllTags.add(tag)
            dbHandler.addTag(tag)
            newTagField.clear
          }

        },
        new Button("Move") {
          minWidth = 75
          alignmentInParent = Pos.Center

          onAction = () => {
            val boolL = leftList.getSelectionModel.selectedItem.isNull()
            val boolR = rightList.getSelectionModel.selectedItem.isNull()
            if !boolL.get then {
              val tag = leftList.getSelectionModel.getSelectedItem
              obsSelcTags.add(tag)
              leftList.getSelectionModel.clearSelection

            } else if !boolR.get then {
              obsSelcTags.remove(rightList.getSelectionModel.getSelectedIndex)
            }
          }
        }
      )
    }
  }

  def getTags: List[String] = {
    obsSelcTags.toList
  }

  def setTags(tList: String) = {
    val trueList = tList.split("\n").toList.map(x => x.trim)
    obsSelcTags.addAll(trueList)
  }

  def clear = {
    obsSelcTags.clear
  }

  def update(tList: String) = {
    obsSelcTags.remove(0, obsSelcTags.size)
    println("lmao")
    setTags(tList)
  }
}
