import os.Path
class FileHandler(inWsl: Boolean):

  def wslPath: Path =
    if inWsl then os.Path("/mnt/") else os.Path("")

  def getRootPath: Path =
    os.Path(s"$wslPath/d/Roleplaying games")

  def getPathFromString(path: String): Path =
    os.Path(s"$wslPath/$path")
