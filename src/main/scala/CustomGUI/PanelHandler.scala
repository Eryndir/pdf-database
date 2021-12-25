import scalafx.scene.layout.BorderPane
import scalafx.scene.control.Label
import State._
import java.util.logging.FileHandler
import scalafx.scene.layout.FlowPane
import scalafx.scene.control.SplitPane

object PanelHandler {

  var panelState: State = CREATE

  val leftCreate = new FilePane
  val rightCreate = new CreationPane

  val createPane = new BorderPane {
    left = leftCreate
    right = rightCreate
  }

  val leftView = new SortingPane
  val centerView = new DataBasePane
  val rightView = new ViewerPane {
    style = " -fx-background-color: white;"
    top = new Label("rightView")
  }

  val viewPane = new SplitPane {
    items ++= Seq(leftView, centerView, rightView)
    visible = false
  }

  def update(state: State): Unit = {
    state match {
      case CREATE => {
        println("CREATE")
        panelState = CREATE
        viewPane.visible = false

        createPane.visible = true
      }
      case VIEW => {
        println("VIEW")
        panelState = VIEW

        viewPane.visible = true

        createPane.visible = false
      }
    }
  }
}

enum State:
  case CREATE
  case VIEW
