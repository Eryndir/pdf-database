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

class ViewerPane extends BorderPane {
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  val pdfWindow = new ImageView {
    alignmentInParent = Pos.Center
  }
  var localPdf = null

  top = pdfWindow

  val openButton = new Button("OPEN")
  val updateButton = new Button("UPDATE")

  val pdfAttributesPane = new PdfAttributesPane(openButton, updateButton)
  bottom = pdfAttributesPane

  def update(pdf: PdfPane) = {
    val pdfName = pdf.text.value

    val pdfSourceString = pdf.source.toString
    val pdfSource = getWinPath(pdfSourceString)
    pdfAttributesPane.update(pdfSource)

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

class PdfAttributesPane(openButton: Button, updateButton: Button)
    extends FlowPane {
  val comboBox = new ComboBox(Category.values.toIndexedSeq) {
    minWidth = 300
  }
  val textArea = new TextArea {
    alignment = Pos.Center
    maxWidth = 170
  }
  val attributePaneSeq = Seq(
    new AttributePane,
    new AttributePane,
    new AttributePane,
    new AttributePane {
      center = comboBox
    },
    new AttributePane {
      textField.setEditable(false)
    },
    new AttributePane {
      textField.setEditable(false)
    },
    new AttributePane,
    new AttributePane,
    new AttributePane,
    new AttributePane,
    new AttributePane {
      center = textArea
    }
  )
  children = attributePaneSeq
  children ++= Seq(openButton, updateButton)

  def update(pdfSource: String) = {
    val pdf = dbHandler.getFromSource(pdfSource)

    attributePaneSeq(0).update(pdf.name)
    attributePaneSeq(1).update(pdf.description)
    attributePaneSeq(2).update(pdf.genre)
    comboBox.getSelectionModel.select(pdf.category)
    attributePaneSeq(4).update(pdf.source)
    attributePaneSeq(5).update(pdf.driveLink)
    attributePaneSeq(6).update(pdf.pageNumbers.toString)
    attributePaneSeq(7).update(pdf.rating)
    attributePaneSeq(8).update(pdf.extraMaterial)
    attributePaneSeq(9).update(pdf.rpg)
    textArea.text = pdf.tagsInString

    updateButton.setOnAction(() => {
      println(attributePaneSeq(10).value)
      val updatedPdf =
        new PdfObject(
          attributePaneSeq(0).value,
          attributePaneSeq(4).value,
          attributePaneSeq(5).value,
          attributePaneSeq(2).value,
          textArea.text.value.split("\n").toList,
          attributePaneSeq(6).value.toInt,
          attributePaneSeq(7).value,
          false,
          false,
          attributePaneSeq(8).value,
          comboBox.getSelectionModel.getSelectedItem,
          rpg = attributePaneSeq(9).value,
          description = attributePaneSeq(1).value
        )
      println(updatedPdf)
      dbHandler.update(updatedPdf)
    })
  }
}

class AttributePane extends BorderPane {
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  val textField = new TextField {
    minWidth = 300
  }
  center = textField

  def update(newText: String) = {
    textField.text = newText
  }

  def value: String = {
    textField.text.value
  }
}
