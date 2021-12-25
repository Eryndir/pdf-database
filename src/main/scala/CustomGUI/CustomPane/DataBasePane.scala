import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control._
import scalafx.scene.layout._
import GUI._
import scalafx.application.Platform
import os.Path
import FilePathHandler._

class DataBasePane extends BorderPane {
  minWidth = (110 * 5) + 5
  val tgFiles = new ToggleGroup
  var allFolders: List[Pane] = List()

  val tiles = new FlowPane
  var mainFolder: FolderPane = null

  var currentFolder: FolderPane = null
  var currentCombo: ComboPane = null

  var loading = false

  center = new ScrollPane {
    content = tiles
  }

  def refresh() = {
    loading = true

    mainFolder = new FolderPane(tiles)
    currentFolder = mainFolder
    allFolders = List()
    val main = new ComboPane("Main Folder", mainFolder, tiles) {
      folder.visible = true
      folder.managed = true
    }
    currentCombo = main

    tiles.children = main
    allFolders = allFolders :+ main.folder

    loading = false
    folderPaneStructure(fileStructure)

  }

  def folderPaneStructure(touple: (Set[String], List[String])) = {

    val foldersSorted = touple._1.toList.sorted
    val entries = touple._2

    val root = foldersSorted.head
    Platform.runLater(() -> {
      foldersSorted.foreach(x => {
        var parentFolder: Pane = tiles

        var parentId = x.substring(0, x.lastIndexOf("\\"))
        if x.equals(root) then parentFolder = mainFolder
        else {
          parentFolder = allFolders.filter(p => p.getId.equals(parentId)).head
        }

        val newFolder = new FolderPane(parentFolder)
        val newCombo = new ComboPane(x, newFolder, tiles)
        currentCombo = newCombo
        parentFolder.children += newCombo
        allFolders = allFolders :+ newCombo.folder

        currentFolder = newFolder
      })

      entries.foreach(x => {
        val folder = x.substring(0, x.lastIndexOf("\\"))
        val path = Path(getUnixPath(x))
        allFolders
          .filter(p => p.getId.equals(folder))
          .head
          .children
          .insert(
            0,
            new PdfPane(path) {
              text = path.last.dropRight(4)
              toggleGroup = tgFiles
            }
          )
      })
    })

  }

  def fileStructure: (Set[String], List[String]) = {
    val entry = dbHandler.getAllFolders()
    var folderPaths: Set[String] = Set()
    var entryPaths: List[String] = List()

    while entry.next do {
      val str = entry.getString(1)
      val path = str.substring(0, str.lastIndexOf("\\"))
      folderPaths = folderPaths + path
      entryPaths = entryPaths :+ str
    }
    (folderPaths, entryPaths)
  }
}
