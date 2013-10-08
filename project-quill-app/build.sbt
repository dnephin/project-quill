name := "ProjectQuill"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test"

play.Project.playScalaSettings
