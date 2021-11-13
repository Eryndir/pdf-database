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
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.MenuButton
import scalafx.scene.control.TextField
import scalafx.scene.layout.VBox
import scalafx.geometry.Insets
import javafx.scene.control.ToggleButton
import scalafx.scene.control.ToggleGroup
import javax.swing.BorderFactory
import scalafx.css.Styleable
import scalafx.scene.layout.FlowPane
import scalafx.scene.layout.TilePane
import scalafx.scene.control.Button
import javafx.scene.control.RadioButton

object GUI extends JFXApp3 {
  def addStyle(item: Styleable, style: String): Unit =
    item.getStyleClass.add(style)

  def addStyles(items: List[Styleable], style: String): Unit =
    items.foreach(_.getStyleClass.add(style))

  def removeStyles(items: List[Styleable], style: String): Unit =
    items.foreach(_.getStyleClass.remove(style))

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Program"
      width = 1200

      height = 800
      scene = new Scene {
        stylesheets = List(getClass.getResource("styles.css").toExternalForm)
        fill = Color.LIGHTGRAY

        val rootPane = new BorderPane

        val menuBar = new TilePane
        addStyle(menuBar, "menuBar")

        val settings = new MenuButton("settings")
        val openDir = new MenuItem("Choose Directory")

        openDir.onAction = (e: ActionEvent) => {
          val directoryChooser = new DirectoryChooser {
            title = "Choose Main Directory"
            initialDirectory = new java.io.File("/mnt/d/")
          }
          val selectedFolder = directoryChooser.showDialog(stage)
          println(selectedFolder)
        }

        settings.items = List(openDir)

        val toggleGroup = new ToggleGroup

        val view = new RadioButton("View")
        val create = new RadioButton("Create")

        view.setToggleGroup(toggleGroup)
        view.setSelected(true)

        addStyles(List(settings, view, create), "toggle-button")
        removeStyles(List(settings, view, create), "radio-button")

        settings.setMaxSize(200, 100);
        view.setMaxSize(200, 100);
        create.setMaxSize(100, 100);

        create.setToggleGroup(toggleGroup)

        val statusBar = new BorderPane

        rootPane.bottom = statusBar
        rootPane.left = menuBar

        menuBar.children ++= List(settings, view, create)

        root = rootPane
      }
    }
  }
}
