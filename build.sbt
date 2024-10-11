ThisBuild / version := "0.1.0-SNAPSHOT"

val languageVersion = "3.5.1"
ThisBuild / scalaVersion := languageVersion

lazy val root = (project in file("."))
  .settings(
    name := "asmd-23",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.18" % Test,
      "org.scalacheck" %% "scalacheck" % "1.17.1" % Test,
      ("de.sciss" %% "scala-chart" % "0.8.0").cross(CrossVersion.for2_13Use3),
      "io.cucumber" %% "cucumber-scala" % "8.25.1" % Test,
      "org.scala-lang" %% "scala3-compiler" % languageVersion,
      "com.lihaoyi" %% "requests" % "0.9.0",
      "com.lihaoyi" %% "upickle" % "3.3.0"
    ),
    Compile / scalaSource := baseDirectory.value / "src" / "main" / "scala",
    Test / scalaSource := baseDirectory.value / "src" / "test" / "scala",
    Test / fork := true,
  )

enablePlugins(CucumberPlugin)
CucumberPlugin.glues := List("features")
// Any environment properties you want to override/set.
CucumberPlugin.envProperties := Map("K" -> "2049")
