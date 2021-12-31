import os.Path
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps
import os.RelPath

object FilePathHandler:
  def getFiles(path: Path): IndexedSeq[Path] = os.list(path)

  def getWinPath(path: String): String =
    s"wslpath -w \"$path\"".!!.stripTrailing

  def getUnixPath(path: String): String =
    s"wslpath -u \"$path\"".!!.stripTrailing

  def openFile(path: String): Unit = s"explorer.exe $path".!

  def getMainPath(): Path =
    Path(getUnixPath(ConfigReader.getProperty("MainPath")))

  def getToReadPath(): Path =
    Path(getUnixPath(ConfigReader.getProperty("ToReadPath")))

  def getNeedSortingPath(): Path =
    Path(getUnixPath(ConfigReader.getProperty("NotSortedPath")))
