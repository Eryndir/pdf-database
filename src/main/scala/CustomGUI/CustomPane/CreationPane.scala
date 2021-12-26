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
import GUI._
import scalafx.scene.control.ComboBox
import scalafx.stage.DirectoryChooser
import os.Path

class CreationPane extends BorderPane {
  padding = new javafx.geometry.Insets(10, 10, 10, 10)
  visible = true

  var comboList: List[ComboText] = List()
  var driveSearch = false
  var pdfDest = ""
  var folderChange = false

  val pdfWindow = new ImageView {
    alignmentInParent = Pos.Center
  }

  val createButton = new Button("CREATE") {
    alignmentInParent = Pos.Center
    visible = false
  }

  val comboBox = new ComboBox(Category.values.toIndexedSeq) {
    minWidth = 400
  }
  comboBox.getSelectionModel.selectLast

  val tagArea = new TextArea {
    maxWidth = 170
  }

  left = new FlowPane {
    //prefWrapLength = 110
    comboList = List(
      new ComboText("Name"),
      new ComboText("Description"),
      new ComboText("Category") {
        right = comboBox
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

        right = tagArea

      }
    )
    children = comboList

    //style = "-fx-border-color: rgb(150, 150, 150)"
  }

  right = new BorderPane {
    top = pdfWindow

    bottom = new FlowPane {
      alignment = Pos.Center
      children = Seq(
        new ComboText("Folder") {
          center = new CheckBox {
            onAction = () => {
              folderChange = !folderChange
            }
          }
          right = new Button("Select Folder") {
            onAction = () => {
              val dirChooser = new DirectoryChooser {
                title = s"Choose folder Directory"
                initialDirectory = new java.io.File("/mnt/d/")
              }
              pdfDest = dirChooser.showDialog(stage).toString
              text = pdfDest
              val newPath = getWinPath(pdfDest)

              val newPdfSource =
                comboList(3).text.substring(comboList(3).text.lastIndexOf("\\"))
              comboList(3).update(getWinPath(pdfDest) + newPdfSource)
              createButton.visible = true
            }
          }
        },
        createButton
      )
    }
  }

  def update(pdf: PdfPane) = {
    if folderChange then createButton.visible = false
    else createButton.visible = true

    createButton.setOnAction(() => {
      Platform.runLater(() -> {
        val childList = pdf.parentCombo.folder.getChildren
        childList.remove(pdf)
        GUI.pool.execute(() => {

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
              tags = tagArea.text.value.split("\n").toList
            )
          )
          if folderChange then os.move.into(pdf.source, Path(pdfDest))
        })
        if childList.size == 0 then
          pdf.parentCombo.folder.parentFolder.getChildren
            .remove(pdf.parentCombo)

        pdfWindow.setImage(null)
        comboList(0).clear()
        comboList(3).clear()
        comboList(4).clear()
        comboList(6).clear()
        comboList(8).clear()
      })

    })

    val pdfName = pdf.text.value
    val pdfSourceString = pdf.source.toString
    val pdfSource = getWinPath(pdfSourceString)

    val clipboard = java.awt.Toolkit.getDefaultToolkit.getSystemClipboard
    val sel = new java.awt.datatransfer.StringSelection(pdfName)
    clipboard.setContents(sel, sel)

    GUI.pool.execute(() => {
      val pdf = PDDocument.load(new File(pdfSourceString))
      val pdfNoPages = pdf.getNumberOfPages()
      val pdfImage = resizeImage(PDFRenderer(pdf).renderImage(0))
      var driveLink = ""

      if driveSearch then driveLink = driveHandler.getFileLink(pdfName)

      Platform.runLater(() -> {
        pdfWindow.image = SwingFXUtils.toFXImage(pdfImage, null)
        comboList(0).update(pdfName)
        comboList(3).update(pdfSource)
        comboList(4).update(driveLink)
        comboList(6).update("" + pdfNoPages)
        comboList(8).update(pdfSource.substring(0, pdfSource.lastIndexOf("\\")))
        GUI.stage.getScene.cursor = Cursor.sfxCursor2jfx(Cursor.Default)
      })
      pdf.close()
    })
  }

  def resizeImage(image: BufferedImage): BufferedImage = {
    Thumbnails.of(image).size(620, 440).asBufferedImage
  }
}
