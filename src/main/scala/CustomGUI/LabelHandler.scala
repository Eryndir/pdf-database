import scalafx.scene.control.Label
import scalafx.application.Platform

object LabelHandler:
  val dirLabel = new Label(
    s"Directory: ${ConfigReader.getProperty("MainPath")}"
  )

  val toReadLabel = new Label(
    s"ToRead: ${ConfigReader.getProperty("ToReadPath")}"
  )

  val needSortingLabel = new Label(
    s"Need Sorting: ${ConfigReader.getProperty("NotSortedPath")}"
  )

  def update(key: String, value: String): Unit =
    Platform.runLater(
      key match
        case "Main" =>
          ConfigReader.setProperty("MainPath", value)
          dirLabel.setText(
            s"Directory: ${ConfigReader.getProperty("MainPath")}"
          )

        case "ToRead" =>
          ConfigReader.setProperty("ToReadPath", value)
          toReadLabel.setText(
            s"ToRead: ${ConfigReader.getProperty("ToReadPath")}"
          )

        case "Need Sorting" =>
          ConfigReader.setProperty("NotSortedPath", value)
          needSortingLabel.setText(
            s"Need Sorting: ${ConfigReader.getProperty("NotSortedPath")}"
          )
    )
