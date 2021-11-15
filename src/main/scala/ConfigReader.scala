package scala

import java.{util => ju}
import java.io._

object ConfigReader:
  def readProperties: ju.Properties =
    val prop = new ju.Properties()
    prop.load(new FileInputStream(new File("config.properties")))
    prop

  def getProperty(key: String): String =
    val prop = readProperties
    prop.getProperty(key)

  def setProperty(key: String, value: String): Unit =
    val prop = readProperties
    prop.setProperty(key, value)
    val fos = new FileOutputStream("config.properties")
    prop.store(fos, null)
    fos.close
