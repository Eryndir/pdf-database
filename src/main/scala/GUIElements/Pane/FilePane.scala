package customGUIElements

import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.layout.BorderPane
import scalafx.scene.control._
import scalafx.scene.layout.TilePane
import javax.swing.filechooser.FileSystemView.getFileSystemView
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.SwingUtilities
import scalafx.embed.swing.SwingFXUtils
import java.awt.image.BufferedImage
import FilePathHandler._

class FilePane extends BorderPane {
  val listFiles = getFiles(getMainPath())
  val listIcons =
    listFiles.map(x => getFileSystemView.getSystemIcon(x.toNIO.toFile))
  val tgFiles = new ToggleGroup

  val tiles = new TilePane {}
  val listComplete = listFiles.zip(listIcons)

  listComplete.foreach((x, y) =>
    tiles.children += new PdfPane {
      text = x.last
      toggleGroup = tgFiles
    }
  )

  center = new ScrollPane {
    content = tiles
  }
}
