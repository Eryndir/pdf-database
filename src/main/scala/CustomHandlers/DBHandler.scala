import java.sql._
import java.sql.DriverManager
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps

class DBHandler:
  var hasData = false
  var con: Connection = null

  def getConnection: Unit =
    Class.forName("org.sqlite.JDBC")
    con = DriverManager.getConnection("jdbc:sqlite:database.db")
    init

  def init: Unit =
    if !hasData then
      hasData = true

      val state = con.createStatement
      val res =
        state.executeQuery(
          "SELECT name from sqlite_master WHERE type ='table' AND name= 'pdfs'"
        )
      if !res.next then
        println("Building the pdf table")
        val state2 = con.createStatement
        state2.execute(
          "CREATE TABLE pdfs(" +
            "name varchar(60), " +
            "source varchar(60), " +
            "driveLink varchar(60)," +
            "genre varchar(60)," +
            "tags varchar(60)," +
            "pageNumbers integer," +
            "rating varchar(3)," +
            "read varchar(10)," +
            "favourite varchar(60)," +
            "extraMaterial varchar(60)," +
            "category varchar(60)," +
            "rpg varchar(60)," +
            "description varchar(60))"
        )

  def addEntry(pdf: pdfObject): Unit =
    if con == null then getConnection
    val prep =
      con.prepareStatement(
        s"INSERT INTO pdfs (name, source, driveLink, genre, tags, pageNumbers, rating, read, favourite, extramaterial, category, rpg, description) " +
          s"values('${pdf.name}','${pdf.source}','${pdf.driveLink}'," +
          s"'${pdf.genre}','${pdf.tags.toString}',${pdf.pageNumbers}," +
          s"'${pdf.rating}','${pdf.read}','${pdf.favourite}','${pdf.extraMaterial}'," +
          s"'${pdf.category.title}','${pdf.rpg}','${pdf.description}')"
      )

    prep.execute

  def searchEntry(name: String): ResultSet =
    if con == null then getConnection
    println("Searching")
    val statement = con.createStatement
    val res = statement.executeQuery(s"SELECT * FROM pdfs Where name='$name'")
    res

  def getAllFolders(): ResultSet =
    if con == null then getConnection
    println("getting Folders...")
    val statement = con.createStatement
    statement.executeQuery("select distinct source from pdfs")

  def displayEntries: ResultSet =
    if con == null then getConnection

    val statement = con.createStatement
    val res = statement.executeQuery("SELECT * FROM pdfs")
    res

  def getRow(res: ResultSet): pdfObject =
    if con == null then getConnection

    val pdf = new pdfObject(
      res.getString(1),
      res.getString(2),
      res.getString(3),
      res.getString(4),
      res.getString(5).split(",").toList,
      res.getInt(6),
      res.getString(7),
      res.getString(8).toBoolean,
      res.getString(9).toBoolean,
      res.getString(10),
      Category.Uncategorized
    )
    pdf

  def emptyTable: Unit =
    if con == null then getConnection
    //println("Are you sure about emptying the table?")
    //val input = io.StdIn.readLine().toString().toLowerCase
    val input = "yes"
    if (input.equals("yes")) {
      val statement = con.prepareStatement("delete from pdfs")
      statement.execute
    }

  def openFile(name: String): Unit =
    val searchRes = searchEntry(name)
    val pdf = getRow(searchRes)
    val pdfSource = s"'${pdf.source}'"
    println(pdfSource)
    s"wslview $pdfSource" !
