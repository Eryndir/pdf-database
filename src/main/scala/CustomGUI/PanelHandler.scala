import scalafx.scene.layout.BorderPane
import scalafx.scene.control.Label
import State._
import java.util.logging.FileHandler

object PanelHandler {

  var panelState: State = CREATE
  val leftCreate = new FilePane
  val rightCreate = new CreationPane
  val leftView = new DataBasePane {
    visible = false
  }
  val rightView = new BorderPane {
    style = " -fx-background-color: white;"
    visible = false
    top = new Label("rightView")
  }

  def update(state: State): Unit = {
    state match {
      case CREATE => {
        panelState = CREATE
        leftCreate.visible = true
        rightCreate.visible = true
        leftView.visible = false
        rightView.visible = false
      }
      case VIEW => {
        panelState = VIEW
        leftCreate.visible = false
        rightCreate.visible = false
        leftView.visible = true
        rightView.visible = true
      }
    }
  }
}

enum State:
  case CREATE
  case VIEW
