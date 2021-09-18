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
  val configReader = new ConfigReader
  println(configReader.getProperty("googleOath"))
  pdfCreation

def consoleReader: String =
  io.StdIn.readLine().toString()

def pdfCreation: Unit =
  val list = List("a", "b")
  val p = new pdfObject(category = Category.CollsCampaigns, categoryInfo = list)
  println(p)
