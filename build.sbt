name := "semver4j"

version := "1.0-SNAPSHOT"


/* Settings */
autoScalaLibrary := false

libraryDependencies += "org.specs2" %% "specs2" % "1.14" % "test"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-explaintypes")

scalaVersion := "2.10.1"
