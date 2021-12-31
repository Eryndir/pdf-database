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
  visible = true

  var comboList: List[ComboText] = List()
  val comboBox = new ComboBox(Category.values.toIndexedSeq) { minWidth = 300 }
  comboBox.getSelectionModel.selectLast

  var driveSearch = false
  val tagArea = new TagArea
  val readCheck = new CheckBox
  val favCheck = new CheckBox

  val pdfWindow = new ImageView { alignmentInParent = Pos.Center }
  var pdfDest = ""
  var folderChange = false
  val createButton = new Button("CREATE"):
    alignmentInParent = Pos.Center
    visible = false

  left = new FlowPane:
    comboList = List(
      new ComboText("Name"),
      new ComboText("Description"),
      new ComboText("Category") { right = comboBox },
      new ComboText("Source"),
      new ComboText("Drive Link"):
        center = new CheckBox:
          onAction = () => driveSearch = !driveSearch
      ,
      new ComboText("Genre"),
      new ComboText("Page Numbers"),
      new ComboText("Rating"),
      new ComboText("Extra Material"):
        center = new Button("CLEAR"):
          onAction = () => textField.clear
      ,
      new ComboText("RPG"),
      new ComboText("Tags") { right = tagArea },
      new ComboText("Read") { right = readCheck },
      new ComboText("Favourite") { right = favCheck }
    )

    children = comboList

  right = new BorderPane:
    top = pdfWindow

    bottom = new FlowPane:
      alignment = Pos.Center
      children = Seq(
        new ComboText("Folder"):
          center = new CheckBox:
            onAction = () => folderChange = !folderChange

          right = new Button("Select Folder"):
            onAction = () =>
              val dirChooser = new DirectoryChooser:
                title = s"Choose folder Directory"
                initialDirectory = new java.io.File("/mnt/d/")

              pdfDest = dirChooser.showDialog(stage).toString
              text = pdfDest

              val newPath = getWinPath(pdfDest)
              val newPdfSource =
                comboList(3).text.substring(comboList(3).text.lastIndexOf("\\"))
              comboList(3).update(getWinPath(pdfDest) + newPdfSource)
              createButton.visible = true
        ,
        createButton
      )

  def update(pdf: PdfPane) =
    if folderChange then createButton.visible = false
    else createButton.visible = true

    createButton.setOnAction(() =>
      Platform.runLater(() =>
        val childList = pdf.parentCombo.folder.getChildren
        childList.remove(pdf)

        GUI.pool.execute(() =>
          val tagList = new TagList
          tagList.addList(tagArea.getTags)

          dbHandler.addEntry(
            new PdfObject(
              name = comboList(0).text,
              description = comboList(1).text,
              category = comboBox.value.value,
              source = comboList(3).text,
              driveLink = comboList(4).text,
              genre = comboList(5).text,
              pageNumbers = comboList(6).text.toInt,
              rating = comboList(7).text,
              extraMaterial = comboList(8).text,
              rpg = comboList(9).text,
              tags = tagList,
              read = readCheck.isSelected,
              favourite = favCheck.isSelected
            )
          )

          if folderChange then os.move.into(pdf.source, Path(pdfDest))
        )

        if childList.size == 0 then
          pdf.parentCombo.folder.parentFolder.getChildren
            .remove(pdf.parentCombo)

        pdfWindow.setImage(null)
        comboList(0).clear
        comboList(3).clear
        comboList(4).clear
        comboList(6).clear
        comboList(8).clear
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
