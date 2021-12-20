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
import customGUIElements._

object GUI extends JFXApp3 {
  override def start(): Unit = {
    val driveHandler = new DriveHandler
    val dbHandler = new DBHandler

    stage = new JFXApp3.PrimaryStage {
      title = "Program"
      width = 1200
      height = 800

      x = 2200
      y = 750

      scene = new Scene {
        stylesheets = List(getClass.getResource("styles.css").toExternalForm)

        val statusBar = new BorderPane {
          styleClass += "statusBar"

          padding = new javafx.geometry.Insets(0, 5, 0, 5)
          left = LabelHandler.dirLabel
          right = LabelHandler.toReadLabel
        }

        val menuBar = new VBox {
          val tg = new ToggleGroup
          styleClass += "menuBar"

          children = Seq(
            new RadioCustom {
              text = "View"
              selected = true
              toggleGroup = tg
            },
            new RadioCustom {
              text = "Create"
              selected = false
              toggleGroup = tg
            },
            new StackPane {
              alignment = Pos.BottomCenter
              vgrow = Priority.Always
              children = new MenuButton("settings") {
                styleClass += "toggle-button"
                minWidth = 100

                items = Seq(
                  new MenuItemCustom("Main", stage),
                  new MenuItemCustom("ToRead", stage)
                )
              }
            }
          )
        }

        val main = new SplitPane {
          items ++= List(
            new StackPane {
              children = Seq(PanelHandler.leftCreate, PanelHandler.leftView)
            },
            new StackPane {
              children = Seq(PanelHandler.rightCreate, PanelHandler.rightView)
            }
          )
        }

        root = new BorderPane {
          bottom = statusBar
          left = menuBar
          center = main
        }
      }
    }
  }
}
