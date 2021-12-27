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
  val tagArea = new TextArea {
    alignment = Pos.Center
    maxWidth = 170
    maxHeight = 100
  }

  val readCheck = new CheckBox
  val favCheck = new CheckBox

  val attributePaneSeq = Seq(
    new AttributePane("Name"),
    new AttributePane("Description"),
    new AttributePane("Genre"),
    new AttributePane("RPG"),
    new AttributePane("Category") {
      center = comboBox
    },
    new AttributePane("Read") {
      center = readCheck
    },
    new AttributePane("Favourite") {
      center = favCheck
    },
    new AttributePane("Source") {
      textField.setEditable(false)
    },
    new AttributePane("Drive Link") {
      textField.setEditable(false)
    },
    new AttributePane("Page Numbers"),
    new AttributePane("Rating"),
    new AttributePane("Extra Material:"),
    new AttributePane("Tags") {
      center = tagArea
    }
  )
  children = attributePaneSeq
  children ++= Seq(openButton, updateButton)

  def update(pdfSource: String) = {
    val pdf = dbHandler.getFromSource(pdfSource)

    attributePaneSeq(0).update(pdf.name)
    attributePaneSeq(1).update(pdf.description)
    attributePaneSeq(2).update(pdf.genre)
    attributePaneSeq(3).update(pdf.rpg)
    comboBox.getSelectionModel.select(pdf.category)
    readCheck.selected = pdf.read
    favCheck.selected = pdf.favourite
    attributePaneSeq(7).update(pdf.source)
    attributePaneSeq(8).update(pdf.driveLink)
    attributePaneSeq(9).update(pdf.pageNumbers.toString)
    attributePaneSeq(10).update(pdf.rating)
    attributePaneSeq(11).update(pdf.extraMaterial)
    tagArea.text = pdf.tagsInString

    updateButton.setOnAction(() => {
      val updatedPdf =
        new PdfObject(
          name = attributePaneSeq(0).value,
          description = attributePaneSeq(1).value,
          genre = attributePaneSeq(2).value,
          rpg = attributePaneSeq(3).value,
          category = comboBox.value.value,
          read = readCheck.isSelected,
          favourite = favCheck.isSelected,
          source = attributePaneSeq(7).value,
          driveLink = attributePaneSeq(8).value,
          pageNumbers = attributePaneSeq(9).value.toInt,
          rating = attributePaneSeq(10).value,
          extraMaterial = attributePaneSeq(11).value,
          tags = tagArea.text.value.split("\n").toList.map(x => x.trim)
        )
      dbHandler.update(updatedPdf)
    })
  }
}
