package customGUIElements

import scalafx.scene.control.Label

object LabelHandler {
  val dirLabel = new Label(
    s"Directory: ${ConfigReader.getProperty("dirPath")}"
  )

  val toReadLabel = new Label(
    s"ToRead: ${ConfigReader.getProperty("toReadPath")}"
  )

  def update(key: String, value: String): Unit = {
    println(value)
    if key.equals("Main") then {
      ConfigReader.setProperty("dirPath", value)
      dirLabel.setText(s"Directory: ${ConfigReader.getProperty("dirPath")}")
    } else {
      ConfigReader.setProperty("toReadPath", value)
      toReadLabel.setText(s"ToRead: ${ConfigReader.getProperty("toReadPath")}")
    }
  }
}
