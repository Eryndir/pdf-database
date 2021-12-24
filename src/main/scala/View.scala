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

object GUI extends JFXApp3 {
  val pool: ExecutorService = Executors.newFixedThreadPool(4)
  override def start(): Unit = {
    val dbHandler = new DBHandler

    stage = new JFXApp3.PrimaryStage {
      title = "GM-Database"
      x = 1950
      y = 750

      maximized = true

      onCloseRequest_=(() => System.exit(0))
      resizable = false
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
            new RadioCustom {
              text = "View"
              selected = false
              toggleGroup = tg
            },
            new RadioCustom {
              text = "Create"
              selected = true
              toggleGroup = tg
            },
            new VBox {
              alignment = Pos.BottomCenter
              vgrow = Priority.Always
              children = Seq(
                new Button {
                  text = "Refresh"
                  styleClass += "toggle-button"
                  minHeight = 50
                  minWidth = 100
                  onAction = (e: ActionEvent) => {
                    PanelHandler.leftCreate.refresh()
                  }

                },
                new MenuButton("settings") {
                  styleClass += "toggle-button"
                  minWidth = 100

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
