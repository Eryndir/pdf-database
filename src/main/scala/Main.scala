import scala.util.control.Breaks._

@main def hello: Unit =
  /*
  breakable {
    while true do
      val input = consoleReader()
      if input.equals("exit") then
        println("break?")
        break
  }


  println("Hello world!")
  println(p.toString)*/
  pdfCreation

def consoleReader: String =
  io.StdIn.readLine().toString()

def pdfCreation: Unit =
  val list = List("a", "b")
  val p = new pdfObject(category = Category.CollsCampaigns, categoryInfo = list)
  println(p)
