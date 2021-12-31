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

class ViewerPane extends BorderPane:
  padding = new javafx.geometry.Insets(10, 10, 10, 10)

  val pdfWindow = new ImageView { alignmentInParent = Pos.Center }

  val openButton = new Button("OPEN")
  val updateButton = new Button("UPDATE")

  val pdfAttributesPane = new PdfAttributesPane(openButton, updateButton)

  top = pdfWindow

  bottom = pdfAttributesPane

  def update(pdf: PdfPane) =
    val pdfName = pdf.text.value

    val pdfSourceString = pdf.source.toString
    val pdfSource = getWinPath(pdfSourceString)
    pdfAttributesPane.update(pdfSource)

    openButton.setOnAction(() => dbHandler.openFile(pdfName))

    GUI.pool.execute(() =>
      val pdf = PDDocument.load(new File(pdfSourceString))
      val pdfNoPages = pdf.getNumberOfPages()
      val pdfImage = resizeImage(PDFRenderer(pdf).renderImage(0))
      pdfWindow.image = SwingFXUtils.toFXImage(pdfImage, null)

      pdf.close()
    )

  def resizeImage(image: BufferedImage): BufferedImage =
    Thumbnails.of(image).size(620, 440).asBufferedImage
