organization := "org.scala-sbt"

name := "sbt-dynver"

version := "1.0-SNAPSHOT"


/* Settings */
libraryDependencies ++= Seq(
  "org.eclipse.jgit" % "org.eclipse.jgit" % "2.1.0.201209190230-r"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-explaintypes")

scalaVersion := "2.10.0"
