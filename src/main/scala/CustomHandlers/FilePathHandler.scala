import os.Path
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps
import os.RelPath

object FilePathHandler:
  def getFiles(path: Path): IndexedSeq[Path] =
    os.walk(path, includeTarget = false)

  def getWinPath(path: String): Path =
    Path(s"wslpath -w \"$path\"".!!.stripTrailing)

  def getUnixPath(path: String): Path =
    Path(s"wslpath -u \"$path\"".!!.stripTrailing)

  def openFile(path: String): Unit =
    s"explorer.exe $path".!

  def convStrToPath(string: String): Path =
    Path(string)

  def getMainPath(): Path =
    getUnixPath(ConfigReader.getProperty("ToReadPath"))
