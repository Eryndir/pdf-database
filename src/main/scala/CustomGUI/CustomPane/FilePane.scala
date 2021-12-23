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
import os.Path
import os.RelPath
import scalafx.scene.layout.FlowPane
import scalafx.geometry.Pos
import scalafx.scene.layout.HBox
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.Pane

class FilePane extends BorderPane {
  prefWidth = 900
  val tgFiles = new ToggleGroup
  var allFolders: List[Pane] = List()
  val debugPath = getToReadPath()

  val tiles = new FlowPane {
    vgap = 10
  }

  val mainFolder = new FolderPane {}
  var currentFolder = mainFolder

  val main = new ComboPane("Main Folder", mainFolder, tiles)
  tiles.children = main
  allFolders = allFolders :+ main.folder

  folderPaneStructure(fileStructure(debugPath))
  println(allFolders)

  center = new ScrollPane {
    content = tiles
  }

  def folderPaneStructure(seq: IndexedSeq[(RelPath, Boolean, Path)]): Unit = {
    println("-" * 40)
    seq.foreach((x, y, z) =>
      if y then
        var parentFolder: Pane = tiles

        if !x.toString.contains("/") then parentFolder = mainFolder
        else {
          val parentId = x.toString.substring(0, x.toString.lastIndexOf("/"))
          println(parentId)
          println(x.toString)
          parentFolder = allFolders.filter(p => p.getId.equals(parentId)).head
        }

        val newFolder = new FolderPane
        val newCombo = new ComboPane(x.toString, newFolder, tiles)

        parentFolder.children += newCombo
        allFolders = allFolders :+ newCombo.folder

        currentFolder = newFolder
        folderPaneStructure(fileStructure(z))
      else
        currentFolder.children += new PdfPane {
          text = x.last.dropRight(4)
          toggleGroup = tgFiles
        }
    )

  }

  def fileStructure(p: Path): IndexedSeq[(RelPath, Boolean, Path)] = {
    val files = getFiles(p)
      .map(x => x.relativeTo(debugPath))
      .lazyZip(getFiles(p).map(os.isDir))
      .lazyZip(getFiles(p))
      .toIndexedSeq

    files.sortBy(_._2)
  }
}
