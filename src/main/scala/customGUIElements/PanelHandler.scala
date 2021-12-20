package customGUIElements

import scalafx.scene.layout.BorderPane
import scalafx.scene.control.Label
import State._
import java.util.logging.FileHandler

object PanelHandler {

  val leftCreate = new FilePane {
    style = " -fx-background-color: red;"
    visible = true
    top = new Label("leftCreate")
  }
  val rightCreate = new BorderPane {
    style = " -fx-background-color: orange;"
    visible = true
    top = new Label("rightCreate")
  }
  val leftView = new FilePane {
    style = " -fx-background-color: black;"
    visible = false
    top = new Label("leftView")
  }
  val rightView = new BorderPane {
    style = " -fx-background-color: white;"
    visible = false
    top = new Label("rightView")
  }

  def update(state: State): Unit = {
    state match {
      case CREATE => {
        leftCreate.visible = true
        rightCreate.visible = true
        leftView.visible = false
        rightView.visible = false
      }
      case VIEW => {
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
