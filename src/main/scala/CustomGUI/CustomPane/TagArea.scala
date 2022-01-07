import scalafx.scene.control._
import scalafx.geometry.Pos
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.application.Platform
import scalafx.stage.Stage
import scalafx.application.JFXApp3
import scalafx.scene.layout._
import scalafx.beans.Observable
import scalafx.collections.ObservableBuffer
import GUI._

class TagArea extends BorderPane:
  maxHeight = 200

  val obsAllTags = new ObservableBuffer[String]
  val obsSelcTags = new ObservableBuffer[String]

  obsAllTags.addAll(dbHandler.getTagList)

  val leftList = new ListView(obsAllTags) { maxWidth = 150 }
  val rightList = new ListView(obsSelcTags) { maxWidth = 150 }

  left = leftList
  right = rightList

  bottom = new BorderPane:
    val newTagField = new TextField { maxWidth = 150 }

    left = newTagField
    right = new HBox:
      children = Seq(
        new Button("Add"):
          minWidth = 75
          alignmentInParent = Pos.Center

          onAction = () =>
            val tag = newTagField.text.value
            if !tag.equals("") then
              obsAllTags.add(tag)
              dbHandler.addTag(tag)
              newTagField.clear
        ,
        new Button("Move"):
          minWidth = 75
          alignmentInParent = Pos.Center

          onAction = () =>
            val boolL = leftList.getSelectionModel.selectedItem.isNull()
            val boolR = rightList.getSelectionModel.selectedItem.isNull()

            if !boolL.get then
              val tag = leftList.getSelectionModel.getSelectedItem
              obsSelcTags.add(tag)
              leftList.getSelectionModel.clearSelection
            else if !boolR.get then
              obsSelcTags.remove(rightList.getSelectionModel.getSelectedIndex)
      )

  def getTags: List[String] = obsSelcTags.toList

  def setTags(tList: String) =
    val trueList = tList.split("\n").toList.map(x => x.trim)
    obsSelcTags.addAll(trueList)

  def clear = obsSelcTags.clear

  def update(tList: String) =
    obsSelcTags.remove(0, obsSelcTags.size)
    setTags(tList)
