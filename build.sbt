val scala3Version = "3.3.5"

assembly / assemblyOutputPath := file("./ttr-scala.jar")

enablePlugins(ScalafmtPlugin)
scalafmtOnCompile := true

enablePlugins(ScoverageSbtPlugin)
coverageEnabled := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "PPS-24-ttr-scala",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    javacOptions ++= Seq("-source", "17.0", "-target", "17.0"),

    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
