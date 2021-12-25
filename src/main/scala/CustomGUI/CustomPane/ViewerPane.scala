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

class ViewerPane extends BorderPane {
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  val pdfWindow = new ImageView
  var localPdf = null

  center = pdfWindow

  val openButton = new Button("OPEN")

  bottom = new FlowPane {
    children = Seq(new ComboText("lmao"), openButton)
  }

  def update(pdf: PdfPane) = {
    val pdfName = pdf.text.value

    val pdfSourceString = pdf.source.toString
    val pdfSource = getWinPath(pdfSourceString)

    openButton.setOnAction(() => {
      dbHandler.openFile(pdfName)
    })

    GUI.pool.execute(() => {
      val pdf = PDDocument.load(new File(pdfSourceString))
      val pdfNoPages = pdf.getNumberOfPages()
      val pdfImage = resizeImage(PDFRenderer(pdf).renderImage(0))
      pdfWindow.image = SwingFXUtils.toFXImage(pdfImage, null)

      pdf.close()
    })
  }
  def resizeImage(image: BufferedImage): BufferedImage = {
    Thumbnails.of(image).size(620, 440).asBufferedImage
  }
}
