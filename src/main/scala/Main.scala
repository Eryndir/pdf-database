import scala.util.control.Breaks._
import scala.util.Properties
import scala.io.Source
import java.{util => ju}
import javax.xml.transform.Source
import java.io.File
import os.Path

//@main
def testDiveHandler: Unit =
  val driveHandler = new DriveHandler
  val dbHandler = new DBHandler
  val entry = pdfCreation(driveHandler)

  dbHandler.addEntry(entry)
  val res = dbHandler.displayEntries

  while res.next do println(dbHandler.getRow(res).toString + "\n")

  println("---")
  dbHandler.openFile("DM Yourself")

def consoleReader: String =
  io.StdIn.readLine().toString()

def pdfCreation(driveHandler: DriveHandler): PdfObject =
  val pdfName = "DM Yourself"
  val pdf = new PdfObject(
    name = pdfName,
    source = s"D:/Roleplaying games/_D&D/3rd Party/Adventures - Solo/$pdfName",
    driveLink = driveHandler.getFileLink(pdfName)
  )
  pdf
