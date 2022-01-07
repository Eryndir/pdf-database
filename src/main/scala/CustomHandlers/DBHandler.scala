import java.sql._
import java.sql.DriverManager
import scala.compiletime.ops.boolean
import scala.sys.process._
import scala.language.postfixOps
import os.Path
import FilePathHandler._

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

  def addEntry(pdf: PdfObject) =
    if con == null then getConnection

    val prep =
      con.prepareStatement(
        s"INSERT INTO pdfs (name, source, driveLink, genre, tags, pageNumbers, rating, read, favourite, extramaterial, category, rpg, description) " +
          s"values(?,?,?," +
          s"?,?,?," +
          s"?,?,?,?," +
          s"?,?,?," +
          s"?,?,?)"
      )

    prep.setString(1, pdf.name)
    prep.setString(2, pdf.source)
    prep.setString(3, pdf.driveLink)
    prep.setString(4, pdf.genre)
    prep.setString(5, pdf.tagsInString)
    prep.setInt(6, pdf.pageNumbers)
    prep.setString(7, pdf.rating)
    prep.setString(8, pdf.read.toString)
    prep.setString(9, pdf.favourite.toString)
    prep.setString(10, pdf.extraMaterial)
    prep.setString(11, pdf.category.title)
    prep.setString(12, pdf.rpg)
    prep.setString(13, pdf.description)
    prep.setString(14, pdf.categoryInfo(0))
    prep.setString(15, pdf.categoryInfo(1))
    prep.setString(16, pdf.categoryInfo(2))

    prep.execute

  def searchEntry(name: String): ResultSet =
    if con == null then getConnection

    val statement = con.createStatement
    val res = statement.executeQuery(s"SELECT * FROM pdfs Where name='$name'")
    res

  def getSearchResult(query: SearchQuery): ResultSet =
    if con == null then getConnection

    val prep = con.prepareStatement(
      "select source from pdfs where " +
        "name like ? and genre like ? and tags like ? and " +
        "pageNumbers > ? and rating like ? and " +
        "read like ? and favourite like ? and " +
        "category like ? and rpg like ?"
    )

    prep.setString(1, "%" + query.name + "%")
    prep.setString(2, "%" + query.genre + "%")
    prep.setString(3, "%" + query.tags.getQuery + "%")
    prep.setInt(4, query.pageNumbers)
    prep.setString(5, "%" + query.rating + "%")

    if query.read then prep.setString(6, query.read.toString)
    else prep.setString(6, "%")

    if query.favourite then prep.setString(7, query.favourite.toString)
    else prep.setString(7, "%")

    prep.setString(8, "%" + query.category.title + "%")
    prep.setString(9, "%" + query.rpg + "%")

    prep.executeQuery

  def displayEntries: ResultSet =
    if con == null then getConnection

    val statement = con.createStatement
    val res = statement.executeQuery("SELECT * FROM pdfs")
    res

  def getRow(res: ResultSet): PdfObject =
    if con == null then getConnection

    val tagList = (new TagList)
    tagList.addList(res.getString(5).split("\n").toList)

    val pdf = new PdfObject(
      res.getString(1),
      res.getString(2),
      res.getString(3),
      res.getString(4),
      tagList,
      res.getInt(6),
      res.getString(7),
      res.getString(8).toBoolean,
      res.getString(9).toBoolean,
      res.getString(10),
      getCategory(res.getString(11)),
      rpg = res.getString(12),
      description = res.getString(13),
      categoryInfo =
        List(res.getString(14), res.getString(15), res.getString(16))
    )

    res.close
    pdf

  def openFile(name: String): Unit =
    if con == null then getConnection

    val searchRes = searchEntry(name)
    val pdf = getRow(searchRes)
    val pdfSource = s"'${pdf.source}'"

    s"wslview $pdfSource" !

  def isInDB(path: Path): Boolean =
    if con == null then getConnection

    val source = getWinPath(path.toString)
    val prep = con.prepareStatement(
      s"select * from pdfs where source = ?"
    )

    prep.setString(1, source)

    val res = prep.executeQuery
    res.next

  def getFromSource(path: String): PdfObject =
    if con == null then getConnection

    val prep = con.prepareStatement(
      s"select * from pdfs where source = ?"
    )

    prep.setString(1, path)

    val res = prep.executeQuery
    getRow(res)

  def update(pdf: PdfObject) =
    if con == null then getConnection

    val prep =
      con.prepareStatement(
        "UPDATE pdfs SET " +
          "name = ?, source = ?, driveLink = ?, genre = ?, " +
          "tags = ?, pageNumbers = ?, rating = ?," +
          "read = ?, favourite = ?, extramaterial = ?, " +
          "category = ?, rpg = ?, description = ? ," +
          "header1 = ?, header2 = ?, header3 = ?" +
          "WHERE source = ?"
      )

    prep.setString(1, pdf.name)
    prep.setString(2, pdf.source)
    prep.setString(3, pdf.driveLink)
    prep.setString(4, pdf.genre)
    prep.setString(5, pdf.tagsInString)
    prep.setInt(6, pdf.pageNumbers)
    prep.setString(7, pdf.rating)
    prep.setString(8, pdf.read.toString)
    prep.setString(9, pdf.favourite.toString)
    prep.setString(10, pdf.extraMaterial)
    prep.setString(11, pdf.category.title)
    prep.setString(12, pdf.rpg)
    prep.setString(13, pdf.description)
    prep.setString(14, pdf.categoryInfo(0))
    prep.setString(15, pdf.categoryInfo(1))
    prep.setString(16, pdf.categoryInfo(2))
    prep.setString(17, pdf.source)

    prep.execute

  def addTag(tag: String) =
    if con == null then getConnection

    val prep = con.prepareStatement("insert into tags (tag) values(?)")

    prep.setString(1, tag)
    prep.execute

  def getTagList: List[String] =
    if con == null then getConnection

    val prep = con.prepareStatement("select tag from tags")
    val res = prep.executeQuery
    var list: List[String] = List()

    while res.next do list = list :+ res.getString(1)

    list

  def getCategory(title: String): Category =
    if con == null then getConnection

    val prep = con.prepareStatement("select * from categories where title = ?")
    prep.setString(1, title)
    val res = prep.executeQuery
    var newTitle = title

    if title.equals("Uncategorized") then newTitle = ""
    new Category(newTitle, res.getString(2), res.getString(3), res.getString(4))

  def getCategoryTitles: List[String] =
    if con == null then getConnection

    val prep = con.prepareStatement("select title from categories")
    val res = prep.executeQuery
    var list: List[String] = List()

    while res.next do list = list :+ res.getString(1)

    list

  def addCategory(category: String, h1: String, h2: String, h3: String) =
    if con == null then getConnection

    val prep = con.prepareStatement(
      "insert into categories (title, header1, header2, header3) values(?, ?, ?, ?)"
    )

    prep.setString(1, category)
    prep.setString(2, h1)
    prep.setString(3, h2)
    prep.setString(4, h3)
    prep.execute
