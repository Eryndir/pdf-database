package customGUIElements

import scalafx.scene.layout.BorderPane
import scalafx.scene.control.Label

object PanelHandler {

  val leftCreate = new BorderPane {
    style = " -fx-background-color: red;"
    visible = false
    center = new Label("leftCreate")
  }
  val rightCreate = new BorderPane {
    style = " -fx-background-color: orange;"
    visible = false
    center = new Label("rightCreate")
  }
  val leftView = new BorderPane {
    style = " -fx-background-color: black;"
    visible = true
    center = new Label("leftView")
  }
  val rightView = new BorderPane {
    style = " -fx-background-color: white;"
    visible = true
    center = new Label("rightView")
  }

  def update(state: State): Unit = {
    state match {
      case State.CREATE => {
        leftCreate.visible = true
        rightCreate.visible = true
        leftView.visible = false
        rightView.visible = false
      }
      case State.VIEW => {
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
