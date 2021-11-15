import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene._
import scalafx.scene.control._
import scalafx.stage.DirectoryChooser
import scalafx.event.ActionEvent
import scalafx.geometry._
import scalafx.css.Styleable
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps

object GUI extends JFXApp3 {
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {

      title = "Program"
      width = 1200

      height = 800
      scene = new Scene {
        stylesheets = List(getClass.getResource("styles.css").toExternalForm)

        val statusBar = new HBox {
          styleClass += "statusBar"
          val dirLabel = new Label("Directory: None")
          children += dirLabel
        }

        val menuBar = new VBox {
          val tg = new ToggleGroup
          styleClass += "menuBar"

          class customRadio(textArg: String, selectedArg: Boolean)
              extends RadioButton {
            text = textArg
            styleClass -= "radio-button"
            styleClass += "toggle-button"
            minHeight = 50
            minWidth = 100
            toggleGroup = tg
            selected = selectedArg
          }

          children = Seq(
            new customRadio("View", true),
            new customRadio("Create", false),
            new StackPane {
              alignment = Pos.BottomCenter
              vgrow = Priority.Always
              children = new MenuButton("settings") {
                styleClass += "toggle-button"
                minWidth = 100

                items = Seq(
                  new MenuItem("Choose Directory") {
                    onAction = (e: ActionEvent) => {
                      val directoryChooser = new DirectoryChooser {
                        title = "Choose Main Directory"
                        initialDirectory = new java.io.File("/mnt/d/")
                      }
                      val selectedFolder =
                        directoryChooser.showDialog(stage).toString

                      val path = "wslpath -w \"/mnt/d/Roleplaying games\"".!!
                      s"explorer.exe $path".!

                      statusBar.getChildren.head match {
                        case l: javafx.scene.control.Label =>
                          l.setText(s"Directory: ${path}")
                        case _ => println("ops")
                      }

                    }
                  }
                )
              }
            }
          )
        }

        root = new BorderPane {
          bottom = statusBar

          left = menuBar
        }
      }
    }
  }
}
