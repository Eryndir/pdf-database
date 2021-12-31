import scalafx.Includes._
import scalafx.Includes.jfxControl2sfx
import scalafx.scene.control._
import scalafx.scene.layout._
import GUI._
import scalafx.application.Platform
import os.Path
import FilePathHandler._
import scalafx.scene.control.Alert.AlertType
import scalafx.geometry.Pos

class DataBasePane extends BorderPane:
  minWidth = (110 * 5) + 5
  maxWidth = (110 * 7) + 5

  val tiles = new FlowPane { alignment = Pos.Center }
  val tgFiles = new ToggleGroup

  var loading = false
  var allFolders: List[Pane] = List()
  var mainFolder: FolderPane = null
  var currentFolder: FolderPane = null
  var currentCombo: ComboPane = null

  center = new ScrollPane { content = tiles }

  def refresh(query: SearchQuery) =
    loading = true

    Platform.runLater(() =>
      mainFolder = new FolderPane(tiles)
      currentFolder = mainFolder
      allFolders = List()

      val main = new ComboPane("D:\\Roleplaying games", mainFolder, tiles):
        folder.visible = true
        folder.managed = true

      currentCombo = main
      tiles.children = main
      allFolders = allFolders :+ main.folder
    )

    folderPaneStructure(fileStructure(query))
    loading = false

    Platform.runLater(() =>
      new Alert(AlertType.Information) {
        initOwner(stage)
        title = "Refresh update"
        headerText = "Refresh is Done"
      }.showAndWait()
    )

  def folderPaneStructure(touple: (Set[String], List[String])) =
    val foldersSorted = touple._1.toList.sorted
    val entries = touple._2
    val root = "D:\\Roleplaying games"

    Platform.runLater(() =>
      foldersSorted.foreach(x =>
        var parentFolder: Pane = tiles

        if x.equals(root) then parentFolder = mainFolder
        else parentFolder = getParentFolder(x)

        val newFolder = new FolderPane(parentFolder)
        val newCombo = new ComboPane(x, newFolder, tiles):
          folder.visible = true
          folder.managed = true

        currentCombo = newCombo
        parentFolder.children += newCombo
        allFolders = allFolders :+ newCombo.folder

        currentFolder = newFolder
      )

      entries.foreach(x =>
        val folder = x.substring(0, x.lastIndexOf("\\"))
        val pdf = dbHandler.getFromSource(x)
        val path = Path(getUnixPath(x))

        allFolders
          .filter(p => p.getId.equals(folder))
          .head
          .children
          .insert(
            0,
            new PdfPane(path):
              text = pdf.name
              toggleGroup = tgFiles
          )
      )
    )

  def fileStructure(query: SearchQuery): (Set[String], List[String]) =
    val entry = dbHandler.getSearchResult(query)

    var folderPaths: Set[String] = Set()
    var entryPaths: List[String] = List()

    while entry.next do
      val str = entry.getString(1)
      val path = str.substring(0, str.lastIndexOf("\\"))

      folderPaths = folderPaths + path
      entryPaths = entryPaths :+ str

    (folderPaths, entryPaths)

  def getParentFolder(parentId: String): Pane =
    val optionList = allFolders.filter(p => p.getId.equals(parentId))
    val newParentId = parentId.substring(0, parentId.lastIndexOf("\\"))

    if optionList.size == 0 then getParentFolder(newParentId)
    else optionList.head
