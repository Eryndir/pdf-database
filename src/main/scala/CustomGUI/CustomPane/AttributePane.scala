import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.image.ImageView
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import FilePathHandler._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import scalafx.embed.swing.SwingFXUtils
import java.io.File
import java.awt.image.BufferedImage
import net.coobird.thumbnailator.Thumbnails
import scalafx.scene.control.Button
import GUI._
import scalafx.application.Platform
import scalafx.scene.control.ComboBox
import scalafx.scene.control.CheckBox
import scalafx.scene.control.TextArea
import scalafx.scene.control.TextField

class AttributePane(lText: String) extends BorderPane {
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  val label = new Label(lText)
  val textField = new TextField {
    minWidth = 300
  }
  top = label
  center = textField

  def update(newText: String) = {
    textField.text = newText
  }

  def value: String = {
    textField.text.value
  }

  def clear() = {
    textField.clear
  }
}
