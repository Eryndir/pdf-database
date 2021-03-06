val scala3Version = "3.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",
    scalaVersion := scala3Version,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test"
  )

//google api dependencies
libraryDependencies += "com.google.api-client" % "google-api-client" % "1.23.0"
libraryDependencies += "com.google.oauth-client" % "google-oauth-client-jetty" % "1.23.0"
libraryDependencies += "com.google.apis" % "google-api-services-drive" % "v3-rev110-1.23.0"

libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.34.0"

libraryDependencies += "com.lihaoyi" %% "os-lib" % "0.7.8"

libraryDependencies += "net.coobird" % "thumbnailator" % "[0.4, 0.5)"
libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.25"
// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R25"
libraryDependencies += "org.controlsfx" % "controlsfx" % "11.1.1"
scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-encoding",
  "utf8",
  "-feature"
)

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _                            => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules =
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m =>
  "org.openjfx" % s"javafx-$m" % "15" classifier osName
)

fork in run := true

javaOptions ++= Seq(
  "-Dprism.order=sw"
)
