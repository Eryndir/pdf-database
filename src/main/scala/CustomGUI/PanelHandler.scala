import scalafx.scene.layout._
import scalafx.scene.control._
import State._
import java.util.logging.FileHandler

object PanelHandler:
  var panelState: State = CREATE

  val leftCreate = new FilePane
  val rightCreate = new CreationPane

  val createPane = new BorderPane:
    left = leftCreate
    right = rightCreate

  val leftView = new SortingPane
  val centerView = new DataBasePane
  val rightView = new ViewerPane

  val viewPane = new SplitPane:
    items ++= Seq(leftView, centerView, rightView)
    visible = false

  def update(state: State): Unit =
    state match
      case CREATE =>
        panelState = CREATE

        viewPane.visible = false
        createPane.visible = true

      case VIEW =>
        panelState = VIEW

        viewPane.visible = true
        createPane.visible = false

enum State:
  case CREATE
  case VIEW
