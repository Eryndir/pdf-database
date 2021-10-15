import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.shape.Rectangle
import javafx.scene.paint.Color
import scalafx.scene.control.{MenuBar, Menu, MenuItem}
import scalafx.stage.DirectoryChooser
import scalafx.event.ActionEvent
import scalafx.scene.input.{KeyCodeCombination, KeyCode}
import scalafx.scene.input.KeyCombination

object GUI extends JFXApp3 {

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Program"
      width = 600

      height = 450
      scene = new Scene {
        fill = Color.LIGHTGRAY
        val menuBar = new MenuBar
        val fileMenu = new Menu("file")

        val openDir = new MenuItem("Choose Directory")
        openDir.accelerator =
          new KeyCodeCombination(KeyCode.O, KeyCombination.ControlDown)
        openDir.onAction = (e: ActionEvent) => {
          val directoryChooser = new DirectoryChooser {
            title = "Choose Main Directory"
            initialDirectory = new java.io.File("/mnt/d/")
          }
          val selectedFolder = directoryChooser.showDialog(stage)
          println(selectedFolder)
        }

        val openToRead = new MenuItem("Choose ToRead Folder")

        fileMenu.items = List(openDir, openToRead)
        menuBar.menus = List(fileMenu)
        menuBar.prefWidth = 600

        content = List(menuBar)
      }

    }
  }
}
