import scalafx.scene.layout._
import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control._
import scalafx.geometry.Pos
import FilePathHandler._
import java.awt.Desktop
import java.net.{URI, URL}
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
import GUI._
import scalafx.stage.DirectoryChooser
import os.Path

class CreationPane extends BorderPane:
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  maxHeight = 900
  visible = true

  var comboList: List[AttributePane] = List()
  val categoryPane = new CategoryPane

  var driveSearch = false
  val systemPane = new SystemPane
  val tagArea = new TagArea

  val readCheck = new CheckBox
  val favCheck = new CheckBox
  val toReadCheck = new CheckBox

  val pdfWindow = new ImageView { alignmentInParent = Pos.Center }
  var pdfDest = ""
  val createButton = new Button("CREATE") { alignmentInParent = Pos.Center }

  left = new FlowPane:
    comboList = List(
      new AttributePane("Name"),
      new AttributePane("Description"),
      categoryPane,
      new AttributePane("Source"),
      new AttributePane("Drive Link"):
        textField.minWidth = 280
        textField.maxWidth = 280
        left = new CheckBox:
          onAction = () => driveSearch = !driveSearch
      ,
      new AttributePane("Genre"),
      new AttributePane("Page Numbers"),
      new AttributePane("Rating"),
      new AttributePane("Extra Material"):
        textField.minWidth = 240
        textField.maxWidth = 240
        left = new Button("CLEAR"):
          onAction = () => textField.clear
      ,
      systemPane,
      new AttributePane("Tags") { center = tagArea },
      new AttributePane("Read") { center = readCheck },
      new AttributePane("Favourite") { center = favCheck },
      new AttributePane("ToRead") { center = toReadCheck }
    )

    prefWrapLength = 50
    children = comboList

  right = new BorderPane:
    minWidth = 750
    top = pdfWindow

    bottom = new FlowPane:
      alignment = Pos.Center
      children = Seq(
        new AttributePane("Folder"):

          center = new Button("Select Folder"):
            onAction = () =>
              val dirChooser = new DirectoryChooser:
                title = s"Choose folder Directory"
                initialDirectory = new java.io.File("/mnt/d/")

              pdfDest = dirChooser.showDialog(stage).toString

              val newPath = getWinPath(pdfDest)
              val newPdfSource =
                comboList(3).value.substring(
                  comboList(3).value.lastIndexOf("\\")
                )
              comboList(3).update(getWinPath(pdfDest) + newPdfSource)
        ,
        createButton
      )

  def update(pdf: PdfPane) =

    createButton.setOnAction(() =>
      Platform.runLater(() =>
        val childList = pdf.parentCombo.folder.getChildren
        childList.remove(pdf)

        GUI.pool.execute(() =>
          val tagList = new TagList
          tagList.addList(tagArea.getTags)
          dbHandler.addEntry(
            new PdfObject(
              name = comboList(0).value,
              description = comboList(1).value,
              category = dbHandler.getCategory(categoryPane.value),
              source = comboList(3).value,
              driveLink = comboList(4).value,
              genre = comboList(5).value,
              pageNumbers =
                if comboList(6).value.equals("") then 0
                else comboList(6).value.toInt,
              rating = comboList(7).value,
              extraMaterial = comboList(8).value,
              rpg = comboList(9).value,
              tags = tagList,
              read = readCheck.isSelected,
              favourite = favCheck.isSelected,
              categoryInfo = List(
                categoryPane.headerValues._1,
                categoryPane.headerValues._2,
                categoryPane.headerValues._3
              ),
              toRead = toReadCheck.isSelected
            )
          )
          try {
            os.move.into(pdf.source, Path(pdfDest))
          } catch {
            case _: Throwable => println("Couldnt move file")
          }
        )

        if childList.size == 0 then
          pdf.parentCombo.folder.parentFolder.getChildren
            .remove(pdf.parentCombo)

        pdfWindow.setImage(null)
      )
    )

    val pdfName = pdf.text.value
    val pdfSourceString = pdf.source.toString
    val pdfSource = getWinPath(pdfSourceString)

    val clipboard = java.awt.Toolkit.getDefaultToolkit.getSystemClipboard
    val sel = new java.awt.datatransfer.StringSelection(pdfName)
    clipboard.setContents(sel, sel)

    GUI.pool.execute(() =>
      val pdf = PDDocument.load(new File(pdfSourceString))
      val pdfNoPages = pdf.getNumberOfPages()
      val pdfImage = resizeImage(PDFRenderer(pdf).renderImage(0))
      var driveLink = ""

      if driveSearch then driveLink = driveHandler.getFileLink(pdfName)

      Platform.runLater(() =>
        pdfWindow.image = SwingFXUtils.toFXImage(pdfImage, null)
        comboList(0).update(pdfName)
        comboList(3).update(pdfSource)
        comboList(4).update(driveLink)
        comboList(6).update("" + pdfNoPages)
        comboList(8).update(pdfSource.substring(0, pdfSource.lastIndexOf("\\")))
        GUI.stage.getScene.cursor = Cursor.sfxCursor2jfx(Cursor.Default)
      )

      pdf.close()
    )

  def resizeImage(image: BufferedImage): BufferedImage =
    Thumbnails.of(image).size(620, 440).asBufferedImage
