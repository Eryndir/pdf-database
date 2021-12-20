package scala

import os.Path
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps
import os.RelPath

object FilePathHandler:
  def getFiles(path: Path): IndexedSeq[Path] =
    println(path)
    val seq = os.walk(path, includeTarget = true)
    seq.foreach(println)
    seq

  def getWinPath(path: String): Path =
    Path(s"wslpath -w \"$path\"".!!.stripTrailing)

  def getUnixPath(path: String): Path =
    Path(s"wslpath -u \"$path\"".!!.stripTrailing)

  def openFile(path: String): Unit =
    s"explorer.exe $path".!

  def convStrToPath(string: String): Path =
    Path(string)

  def getMainPath(): Path =
    FilePathHandler.getUnixPath(ConfigReader.getProperty("ToReadPath"))
