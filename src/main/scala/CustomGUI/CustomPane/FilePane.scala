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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import scalafx.application.Platform

class FilePane extends BorderPane {
  minWidth = (110 * 5) + 5
  val tgFiles = new ToggleGroup
  var allFolders: List[Pane] = List()

  val debugPath = getToReadPath()

  val tiles = new FlowPane
  var mainFolder: FolderPane = null

  var currentFolder: FolderPane = null

  var loadingFinished = false
  GUI.pool.execute(() => {
    refresh()
    loadingFinished = true
  })

  center = new ScrollPane {
    content = tiles
  }

  def refresh() = {
    mainFolder = new FolderPane {}
    currentFolder = mainFolder

    val main = new ComboPane("Main Folder", mainFolder, tiles) {
      folder.visible = true
      folder.managed = true
    }

    tiles.children = main
    allFolders = allFolders :+ main.folder

    folderPaneStructure(fileStructure(debugPath))
    (mainFolder, currentFolder)
  }

  def folderPaneStructure(seq: IndexedSeq[(RelPath, Boolean, Path)]): Unit = {
    seq.foreach((x, y, z) =>
      if y then
        Platform.runLater(
          () -> {
            var parentFolder: Pane = tiles

            if !x.toString.contains("/") then parentFolder = mainFolder
            else {
              val parentId =
                x.toString.substring(0, x.toString.lastIndexOf("/"))
              parentFolder =
                allFolders.filter(p => p.getId.equals(parentId)).head
            }

            val newFolder = new FolderPane
            val newCombo = new ComboPane(x.toString, newFolder, tiles)
            parentFolder.children += newCombo
            allFolders = allFolders :+ newCombo.folder

            currentFolder = newFolder
          }
        );

        folderPaneStructure(fileStructure(z))
      else
        Platform.runLater(() -> {
          currentFolder.children += new PdfPane(z) {
            text = x.last.dropRight(4)
            toggleGroup = tgFiles
          }
        })
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
