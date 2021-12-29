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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import State._

object GUI extends JFXApp3 {
  val pool: ExecutorService = Executors.newFixedThreadPool(4)
  val driveHandler = new DriveHandler
  val dbHandler = new DBHandler

  override def start(): Unit = {

    stage = new JFXApp3.PrimaryStage {
      title = "GM-Database"
      x = 1920
      y = 680
      height = 1000

      maximized = true

      onCloseRequest_=(() => System.exit(0))

      scene = new Scene {
        stylesheets = List(getClass.getResource("styles.css").toExternalForm)

        val statusBar = new BorderPane {
          styleClass += "statusBar"

          padding = new javafx.geometry.Insets(0, 5, 0, 5)
          left = LabelHandler.dirLabel
          right = LabelHandler.toReadLabel
          center = LabelHandler.needSortingLabel
        }

        val menuBar = new VBox {
          val tg = new ToggleGroup
          styleClass += "menuBar"

          children = Seq(
            new RadioCustom("View", tg, false),
            new RadioCustom("Create", tg, true),
            new VBox {
              alignment = Pos.BottomCenter
              vgrow = Priority.Always

              children = Seq(
                new Button {
                  text = "Refresh"
                  styleClass += "toggle-button"
                  minHeight = 50
                  minWidth = 100

                  onAction = (e: ActionEvent) =>
                    PanelHandler.panelState match
                      case CREATE => PanelHandler.leftCreate.refresh()
                      case VIEW =>
                        PanelHandler.centerView.refresh(new SearchQuery)

                },
                new MenuButton("settings") {
                  styleClass += "toggle-button"
                  minWidth = 100
                  popupSide = Side.Top

                  items = Seq(
                    new MenuItemCustom("Main", stage),
                    new MenuItemCustom("ToRead", stage),
                    new MenuItemCustom("Need Sorting", stage)
                  )
                }
              )
            }
          )
        }

        val main = new StackPane {
          children = Seq(
            PanelHandler.createPane,
            PanelHandler.viewPane
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
