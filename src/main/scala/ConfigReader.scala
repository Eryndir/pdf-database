import scala.util.Properties
import java.{util => ju}
import java.io.File
import java.io.InputStream
import java.io.FileInputStream
import javax.xml.transform.Source
class ConfigReader:
  def readProperties: ju.Properties =
    val prop = new ju.Properties()
    prop.load(new FileInputStream(new File("config.properties")))
    prop

  def getProperty(key: String): String =
    val prop = readProperties
    prop.getProperty(key)
