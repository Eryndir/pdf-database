package customGUIElements

import scalafx.Includes._
import scalafx.scene.control._
import scalafx.event.ActionEvent
import scalafx.stage.DirectoryChooser
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.control.Alert.AlertType
import scalafx.application.Platform
import FilePathHandler._

class MenuItemCustom(name: String, stage: PrimaryStage) extends MenuItem {
  text = name
  onAction = (e: ActionEvent) => {
    val directoryChooser = new DirectoryChooser {
      title = s"Choose $name Directory"
      initialDirectory = new java.io.File("/mnt/d/")
    }
    val selectedFolder =
      directoryChooser.showDialog(stage).toString

    val previousFolder = ConfigReader.getProperty(s"${name}Path")

    val alert = new Alert(AlertType.Confirmation) {
      initOwner(stage)
      title = "Confirmation Dialog"
      headerText =
        s"About to change $name directory from \n$previousFolder \n\tto \n$selectedFolder"
      contentText = "Are you sure?"
    }

    val result = alert.showAndWait()

    result match {
      case Some(ButtonType.OK) => {
        val path = getWinPath(selectedFolder)
        LabelHandler.update(name, path.toString)
      }
      case _ => println("Cancel or closed")
    }

  }
}
