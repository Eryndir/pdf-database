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
import FilePathHandler._
import java.awt.Desktop
import java.net.{URI, URL}
import scalafx.scene.control.CheckBox
import org.apache.pdfbox._
import org.apache.pdfbox.pdmodel.PDDocument
import java.io.File
import scalafx.application.Platform
import scalafx.scene.Cursor
import org.apache.pdfbox.rendering.PDFRenderer
import scalafx.scene.image.Image
import scalafx.embed.swing.SwingFXUtils
import scalafx.scene.image.ImageView
import java.awt.image.BufferedImage
import net.coobird.thumbnailator.Thumbnails
import javafx.css.PseudoClass

class CreationPane extends BorderPane {
  val driveHandler = new DriveHandler
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  visible = true
  var comboList: List[ComboText] = List()

  var driveSearch = false

  val pdfWindow = new ImageView() {
    alignmentInParent = Pos.Center
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
      new ComboText("driveLink") {
        center = new CheckBox {
          onAction = () => {
            driveSearch = !driveSearch
          }
        }
      },
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
    val pdfSourceString = pdf.source.toString
    val pdfSource = getWinPath(pdfSourceString)

    val clipboard = java.awt.Toolkit.getDefaultToolkit.getSystemClipboard
    val sel = new java.awt.datatransfer.StringSelection(pdfName)
    clipboard.setContents(sel, sel)

      val pdf = PDDocument.load(new File(pdfSourceString))
      val pdfNoPages = pdf.getNumberOfPages()
      val pdfImage = resizeImage(PDFRenderer(pdf).renderImage(0))
      var driveLink = ""

      if driveSearch then driveLink = driveHandler.getFileLink(pdfName)

        pdfWindow.image = SwingFXUtils.toFXImage(pdfImage, null)
    comboList(0).update(pdfName)
    comboList(3).update(pdfSource)
        comboList(4).update(driveLink)
        comboList(6).update("" + pdfNoPages)
        comboList(8).update(pdfSource.substring(0, pdfSource.lastIndexOf("\\")))
        GUI.stage.getScene.cursor = Cursor.sfxCursor2jfx(Cursor.Default)
      pdf.close()
      println("PDF info gotten")
  def resizeImage(image: BufferedImage): BufferedImage = {
    Thumbnails.of(image).scale(0.60).asBufferedImage
  }
}
