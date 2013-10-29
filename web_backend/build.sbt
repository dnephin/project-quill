name := "ProjectQuill"

version := "1.0-SNAPSHOT"

// TODO: remove unused dependencies
libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test",
)

play.Project.playScalaSettings
