import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control.TextField
import scalafx.scene.control.Label
import scalafx.geometry.Pos

class HeaderAttributePane extends AttributePane:
  alignmentInParent = Pos.Center

  val h1 = new TextField { maxWidth = 75 }
  val lh1 = new Label("")

  val h2 = new TextField { maxWidth = 75 }
  val lh2 = new Label("")

  val h3 = new TextField { maxWidth = 75 }
  val lh3 = new Label("")

  top = null

  left = new BorderPane:
    top = lh1
    bottom = h1

  center = new BorderPane:
    top = lh2
    bottom = h2

  right = new BorderPane:
    top = lh3
    bottom = h3

  def updateLabel(strh1: String, strh2: String, strh3: String) =
    lh1.text = strh1
    lh2.text = strh2
    lh3.text = strh3

  def updateValue(strh1: String, strh2: String, strh3: String) =
    h1.text = strh1
    h2.text = strh2
    h3.text = strh3

  def valueTouple: (String, String, String) =
    (h1.text.value, h2.text.value, h3.text.value)
