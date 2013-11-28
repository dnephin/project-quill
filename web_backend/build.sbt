name := "Project Quill"

version := "1.0-development"

resolvers ++= Seq(
    Resolver.url("SBT plugin releases",
                 new URL("http://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"))
                 (Resolver.ivyStylePatterns),
    // NOTE: only required for play2-memcache
    "Spy Repository" at "http://files.couchbase.com/maven2"
)


// TODO: remove unused dependencies
libraryDependencies ++= Seq(
  cache,
  "securesocial" %% "securesocial" % "2.1.2",
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test",
  // NOTE: required to persist dev session between server restarts
  "com.github.mumoshu" %% "play2-memcached" % "0.5.0-RC1"
)

play.Project.playScalaSettings
