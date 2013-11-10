name := "Project Quill"

version := "1.0-development"

resolvers ++= Seq(
    Resolver.url("SBT plugin releases",
                 new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))
                 (Resolver.ivyStylePatterns)
)


// TODO: remove unused dependencies
libraryDependencies ++= Seq(
  cache,
  "securesocial" %% "securesocial" % "2.1.2",
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test"
)

play.Project.playScalaSettings
