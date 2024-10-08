ThisBuild / version := "0.1.0-SNAPSHOT"

val languageVersion = "3.3.0"
ThisBuild / scalaVersion := languageVersion

lazy val root = (project in file("."))
  .settings(
    name := "course-asmd23-models",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.12" % Test,
      "org.scalacheck" %% "scalacheck" % "1.17.0",
      ("de.sciss" %% "scala-chart" % "0.8.0").cross(CrossVersion.for2_13Use3),
      "io.cucumber" %% "cucumber-scala" % "8.14.1" % Test,
      "org.scala-lang" %% "scala3-compiler" % languageVersion,
    ),
    Compile / scalaSource := baseDirectory.value / "src" / "main",
    Test / scalaSource := baseDirectory.value / "src" / "test"
  )

enablePlugins(CucumberPlugin)
CucumberPlugin.glues := List("scala.u07.features")
// Any environment properties you want to override/set.
CucumberPlugin.envProperties := Map("K" -> "2049")
