import os.Path
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps
import os.RelPath

object FilePathHandler:
  def getFiles(path: Path): IndexedSeq[Path] =
    os.list(path)

  def getWinPath(path: String): String =
    val lmao = s"wslpath -w \"$path\"".!!
    lmao.stripTrailing

  def getUnixPath(path: String): String =
    val lmao = s"wslpath -u \"$path\"".!!
    lmao.stripTrailing

  def openFile(path: String): Unit =
    s"explorer.exe $path".!

  def convStrToPath(string: String): Path =
    Path(string)

  def getMainPath(): Path =
    convStrToPath(getUnixPath(ConfigReader.getProperty("MainPath")))

  def getToReadPath(): Path =
    convStrToPath(getUnixPath(ConfigReader.getProperty("ToReadPath")))

  def getNeedSortingPath(): Path =
    convStrToPath(getUnixPath(ConfigReader.getProperty("NotSortedPath")))
