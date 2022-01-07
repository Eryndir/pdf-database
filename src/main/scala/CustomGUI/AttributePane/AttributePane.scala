import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.image.ImageView
import scalafx.geometry.Pos
import FilePathHandler._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import scalafx.embed.swing.SwingFXUtils
import java.io.File
import java.awt.image.BufferedImage
import net.coobird.thumbnailator.Thumbnails
import GUI._
import scalafx.application.Platform
import scalafx.scene.control._

class AttributePane(lText: String = "", creationPane: Boolean = false)
    extends BorderPane:
  padding = new javafx.geometry.Insets(10, 10, 10, 10)

  val label = new Label(lText)
  val textField = new TextField { minWidth = 300 }

  top = label
  center = textField

  def update(newText: String) = textField.text = newText

  def value: String = textField.getText

  def clear = textField.clear
