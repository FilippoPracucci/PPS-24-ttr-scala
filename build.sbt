val scala3Version = "3.3.5"

assembly / assemblyOutputPath := file("./ttr-scala.jar")

enablePlugins(ScalafmtPlugin)
scalafmtOnCompile := true

enablePlugins(ScoverageSbtPlugin)
assembly / coverageEnabled := false
test / coverageEnabled := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "PPS-24-ttr-scala",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    javacOptions ++= Seq("-source", "17.0", "-target", "17.0"),

    libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "3.2.19" % Test,
        "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
        "com.github.vlsi.mxgraph" % "jgraphx" % "4.2.2",
        "com.lihaoyi" %% "upickle" % "4.2.1"
    )
  )
