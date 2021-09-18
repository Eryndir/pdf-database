import java.sql._
import java.sql.DriverManager

class DBHandler:
  var hasData = false;
  var con: Connection = null

  def getConnection: Unit =
    Class.forName("org.sqlite.JDBC")
    con = DriverManager.getConnection("jdbc:sqlite:TESTDB.db")
    init

  def displayUsers: ResultSet =
    if con == null then getConnection

    val state = con.createStatement
    val res = state.executeQuery("SELECT fname, lname FROM user")
    res

  def init: Unit =
    if !hasData then
      hasData = true
      val state = con.createStatement
      val res =
        state.executeQuery(
          "SELECT name from sqlite_master WHERE type ='table' AND name= 'user'"
        )
      if !res.next then
        println("Building the usertable")
        val state2 = con.createStatement
        state2.execute(
          "CREATE TABLE user(id integer, fName varchar(60), " +
            "lName varchar(60), primary key(id))"
        )

        val prep = con.prepareStatement("INSERT INTO user values(?,?,?)")
        prep.setString(2, "John")
        prep.setString(3, "McNeil")
        prep.execute

        val prep2 = con.prepareStatement("INSERT INTO user values(?,?,?)")
        prep2.setString(2, "Paul")
        prep2.setString(3, "Smith")
        prep2.execute

  def addUser(fName: String, lName: String): Unit =
    if con == null then getConnection

    val prep = con.prepareStatement("INSERT INTO user values(?,?,?)")
    prep.setString(2, fName)
    prep.setString(3, lName)
    prep.execute
