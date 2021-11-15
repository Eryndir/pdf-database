package customGUIElements

import scalafx.scene.control.Label
import scalafx.application.Platform

object LabelHandler {
  val dirLabel = new Label(
    s"Directory: ${ConfigReader.getProperty("MainPath")}"
  )

  val toReadLabel = new Label(
    s"ToRead: ${ConfigReader.getProperty("ToReadPath")}"
  )

  def update(key: String, value: String): Unit = {
    Platform.runLater(
      if key.equals("Main") then {
        ConfigReader.setProperty("MainPath", value)
        dirLabel.setText(s"Directory: ${ConfigReader.getProperty("MainPath")}")
      } else {
        ConfigReader.setProperty("ToReadPath", value)
        toReadLabel.setText(
          s"ToRead: ${ConfigReader.getProperty("ToReadPath")}"
        )
      }
    )
  }
}
