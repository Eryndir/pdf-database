import scala.util.control.Breaks._
import scala.util.Properties
import scala.io.Source
import java.{util => ju}
import javax.xml.transform.Source
import java.io.File

@main def main: Unit =
  /*
  breakable {
    while true do
      val input = consoleReader()
      if input.equals("exit") then
        println("break?")
        break
  }


  println("Hello world!")*/
  val driveHandler = new DriveHandler
  driveHandler
    .searchFile("Läsår")
    .foreach(i => println(s"${i}"))
  pdfCreation(driveHandler)

def consoleReader: String =
  io.StdIn.readLine().toString()

def pdfCreation(driveHandler: DriveHandler): Unit =
  val list = List("a", "b")
  val pdfName = "Arcadia #006"
  val p = new pdfObject(
    name = pdfName,
    driveLink = driveHandler.getFileLink(pdfName),
    category = Category.CollsCampaigns,
    categoryInfo = list
  )
  println(p)
