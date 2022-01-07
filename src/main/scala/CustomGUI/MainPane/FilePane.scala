import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.layout._
import scalafx.scene.control._
import javax.swing.filechooser.FileSystemView.getFileSystemView
import scalafx.scene.image._
import javax.swing.ImageIcon
import javax.swing.SwingUtilities
import scalafx.embed.swing.SwingFXUtils
import java.awt.image.BufferedImage
import FilePathHandler._
import os.Path
import os.RelPath
import scalafx.geometry.Pos
import scalafx.scene.input.MouseEvent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import scalafx.application.Platform
import GUI._
import scalafx.scene.control.Alert.AlertType

class FilePane extends BorderPane:
  minWidth = (110 * 6) + 5

  val tiles = new FlowPane
  val tgFiles = new ToggleGroup

  var loading = false
  var allFolders: List[Pane] = List()
  var mainFolder: FolderPane = null
  var currentFolder: FolderPane = null
  var currentCombo: ComboPane = null

  center = new ScrollPane { content = tiles }

  def refresh() =
    loading = true

    Platform.runLater(() =>
      mainFolder = new FolderPane(tiles)
      currentFolder = mainFolder
      allFolders = List()

      val main = new ComboPane("Main Folder", mainFolder, tiles):
        folder.visible = true
        folder.managed = true

      currentCombo = main
      tiles.children = main
      allFolders = allFolders :+ main.folder
    )

    folderPaneStructure(fileStructure(getNeedSortingPath()))
    loading = false

    Platform.runLater(() =>
      new Alert(AlertType.Information) {
        initOwner(stage)
        title = "Refresh update"
        headerText = "Refresh is Done"
      }.showAndWait()
    )

  def folderPaneStructure(seq: IndexedSeq[(RelPath, Boolean, Path)]): Unit =
    seq.foreach((x, y, z) =>
      if y then
        Platform.runLater(() =>
          var parentFolder: Pane = tiles

          if !x.toString.contains("/") then parentFolder = mainFolder
          else
            val parentId =
              x.toString.substring(0, x.toString.lastIndexOf("/"))
            parentFolder = allFolders.filter(p => p.getId.equals(parentId)).head

          val newFolder = new FolderPane(parentFolder)
          val newCombo = new ComboPane(x.toString, newFolder, tiles)

          currentCombo = newCombo
          parentFolder.children += newCombo
          allFolders = allFolders :+ newCombo.folder

          currentFolder = newFolder
        )

        folderPaneStructure(fileStructure(z))
      else
        Platform.runLater(() =>
          if !dbHandler.isInDB(z) then
            currentFolder.children += new PdfPane(z, currentCombo):
              text = x.last.dropRight(4)
              toggleGroup = tgFiles
        )
    )

  def fileStructure(p: Path): IndexedSeq[(RelPath, Boolean, Path)] =
    val files = getFiles(p)
      .map(x => x.relativeTo(getNeedSortingPath()))
      .lazyZip(getFiles(p).map(os.isDir))
      .lazyZip(getFiles(p))
      .toIndexedSeq

    files.sortBy(_._2)
