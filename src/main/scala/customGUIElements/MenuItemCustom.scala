package customGUIElements

import scalafx.Includes._
import scalafx.scene.control.MenuItem
import scalafx.event.ActionEvent
import scalafx.stage.DirectoryChooser
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps
import scalafx.application.JFXApp3.PrimaryStage

class MenuItemCustom(name: String, stage: PrimaryStage) extends MenuItem {
  text = name
  onAction = (e: ActionEvent) => {
    val directoryChooser = new DirectoryChooser {
      title = s"Choose $name Directory"
      initialDirectory = new java.io.File("/mnt/d/")
    }
    val selectedFolder =
      directoryChooser.showDialog(stage).toString

    val path = s"wslpath -w \"$selectedFolder\"".!!
    s"explorer.exe $path".!

    LabelHandler.update(name, path)
  }
}
