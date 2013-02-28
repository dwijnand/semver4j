organization := "org.scala-sbt"

name := "dynver"

version := "1.0-SNAPSHOT"


/* Settings */
autoScalaLibrary := false

libraryDependencies ++= Seq(
  "org.eclipse.jgit" % "org.eclipse.jgit" % "2.1.0.201209190230-r",
  "org.specs2" %% "specs2" % "1.13" % "test"
)

sbtPlugin := true

scalacOptions ++= Seq("-deprecation", "-unchecked", "-explaintypes")

scalaVersion := "2.10.0"
