import scalafx.scene.layout.BorderPane
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control.Label
import scalafx.geometry.Pos
import scalafx.scene.layout.FlowPane
import scalafx.scene.control.TextArea
import scalafx.scene.control.TextField
import scalafx.scene.control.MenuItem
import scalafx.scene.control.MenuButton
import collection.JavaConverters._
import scalafx.scene.control.Button
import java.awt.Desktop
import java.net.{URI, URL}

class CreationPane extends BorderPane {
  val driveHandler = new DriveHandler
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  visible = true
  var comboList: List[ComboText] = List()

  val pdfWindow = new PdfPane {
    styleClass -= "pdf-pane"
    styleClass += "pdf-window"
    minHeight = 155 * 2
    minWidth = 110 * 2

    maxHeight = 155 * 2
    maxWidth = 110 * 2
  }

  left = new FlowPane {
    prefWrapLength = 110
    comboList = List(
      new ComboText("Name"),
      new ComboText("Description"),
      new ComboText("Category") {

        val menu = new MenuButton {}
        val lmao =
          Category.values.map(x => new javafx.scene.control.MenuItem(x.title))

        val oof = scala.collection.JavaConverters.asJavaCollection(lmao)
        menu.getItems.setAll(oof)
        right = menu
      },
      new ComboText("Source"),
      new ComboText("driveLink"),
      new ComboText("genre"),
      new ComboText("pageNumbers"),
      new ComboText("rating"),
      new ComboText("extraMaterial") {
        center = new Button("CLEAR") {
          onAction = () => {
            textfield.clear
          }
        }
      },
      new ComboText("rpg"),
      new ComboText("tags") {
        right = new TextArea {
          maxWidth = 170
        }
      }
    )
    children = comboList

    style = "-fx-border-color: rgb(150, 150, 150)"
  }

  right = new BorderPane {
    top = pdfWindow

    center = new Button("CREATE")
  }
  def update(pdf: PdfPane) = {
    val pdfName = pdf.text.value
    val pdfSource = pdf.source.toString

    val clipboard = java.awt.Toolkit.getDefaultToolkit.getSystemClipboard
    val sel = new java.awt.datatransfer.StringSelection(pdfName)
    clipboard.setContents(sel, sel)

    pdfWindow.text = pdfName
    comboList(0).update(pdfName)
    comboList(3).update(pdfSource)
    comboList(4).update(driveHandler.getFileLink(pdfName))
    comboList(8).update(pdfSource.substring(0, pdfSource.lastIndexOf("/")))
  }
}
