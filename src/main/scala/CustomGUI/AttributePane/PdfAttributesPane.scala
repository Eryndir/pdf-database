import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.image.ImageView
import scalafx.geometry.Pos
import scalafx.scene.control._
import FilePathHandler._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.PDFRenderer
import scalafx.embed.swing.SwingFXUtils
import java.io.File
import java.awt.image.BufferedImage
import net.coobird.thumbnailator.Thumbnails
import GUI._
import scalafx.application.Platform

class PdfAttributesPane(openButton: Button, updateButton: Button)
    extends BorderPane:
  val categoryPane = new CategoryPane
  val tagArea = new TagArea

  val readCheck = new CheckBox
  val favCheck = new CheckBox

  val attributePaneSeq = Seq(
    new AttributePane("Name"),
    new AttributePane("Description"),
    new AttributePane("Genre"),
    new AttributePane("RPG"),
    categoryPane,
    new AttributePane("Read") { center = readCheck },
    new AttributePane("Favourite") { center = favCheck },
    new AttributePane("Source") { textField.setEditable(false) },
    new AttributePane("Drive Link") { textField.setEditable(false) },
    new AttributePane("Page Numbers"):
      textField.minWidth = 100
      textField.maxWidth = 100
    ,
    new AttributePane("Rating"):
      textField.minWidth = 100
      textField.maxWidth = 100
    ,
    new AttributePane("Extra Material:"),
    new AttributePane("Tags") { center = tagArea }
  )

  val leftPane = attributePaneSeq.take(9)
  val rightPane = attributePaneSeq.drop(9)

  left = new FlowPane:
    prefWrapLength = 100

    children = leftPane

  right = new FlowPane:
    prefWrapLength = 100

    children = rightPane
    children += new BorderPane:
      alignment = Pos.Center
      top = openButton
      bottom = updateButton

  def update(pdfSource: String) =
    val pdf = dbHandler.getFromSource(pdfSource)

    attributePaneSeq(0).update(pdf.name)
    attributePaneSeq(1).update(pdf.description)
    attributePaneSeq(2).update(pdf.genre)
    attributePaneSeq(3).update(pdf.rpg)
    categoryPane.comboBox.getSelectionModel.select(pdf.category.title)
    readCheck.selected = pdf.read
    favCheck.selected = pdf.favourite
    attributePaneSeq(7).update(pdf.source)
    attributePaneSeq(8).update(pdf.driveLink)
    attributePaneSeq(9).update(pdf.pageNumbers.toString)
    attributePaneSeq(10).update(pdf.rating)
    attributePaneSeq(11).update(pdf.extraMaterial)
    tagArea.update(pdf.tagsInString)
    categoryPane.updateValue(
      pdf.categoryInfo(0),
      pdf.categoryInfo(1),
      pdf.categoryInfo(2)
    )

    val pdfCat = pdf.category
    categoryPane.headers.updateLabel(
      pdfCat.header1,
      pdfCat.header2,
      pdfCat.header3
    )

    updateButton.setOnAction(() =>
      val tagList = new TagList
      tagList.addList(tagArea.getTags)

      val updatedPdf =
        new PdfObject(
          name = attributePaneSeq(0).value,
          description = attributePaneSeq(1).value,
          genre = attributePaneSeq(2).value,
          rpg = attributePaneSeq(3).value,
          category = dbHandler.getCategory(categoryPane.value),
          read = readCheck.isSelected,
          favourite = favCheck.isSelected,
          source = attributePaneSeq(7).value,
          driveLink = attributePaneSeq(8).value,
          pageNumbers = attributePaneSeq(9).value.toInt,
          rating = attributePaneSeq(10).value,
          extraMaterial = attributePaneSeq(11).value,
          tags = tagList,
          categoryInfo = List(
            categoryPane.headerValues._1,
            categoryPane.headerValues._2,
            categoryPane.headerValues._3
          )
        )

      dbHandler.update(updatedPdf)
    )
